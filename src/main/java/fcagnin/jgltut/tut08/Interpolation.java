package fcagnin.jgltut.tut08;

import fcagnin.jglsdk.glm.Glm;
import fcagnin.jglsdk.glm.Mat4;
import fcagnin.jglsdk.glm.Quaternion;
import fcagnin.jglsdk.glm.Vec4;
import fcagnin.jglsdk.glutil.MatrixStack;
import fcagnin.jgltut.LWJGLWindow;
import fcagnin.jgltut.framework.Framework;
import fcagnin.jgltut.framework.Mesh;
import fcagnin.jgltut.framework.Timer;
import org.lwjgl.BufferUtils;
import org.lwjgl.input.Keyboard;

import java.nio.FloatBuffer;
import java.util.ArrayList;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL20.*;


/**
 * Visit https://github.com/integeruser/jgltut for info, updates and license terms.
 * <p/>
 * Part II. Positioning
 * Chapter 8. Getting Oriented
 * http://www.arcsynthesis.org/gltut/Positioning/Tutorial%2008.html
 * <p/>
 * SPACE            - toggles between regular linear interpolation and slerp.
 * Q,W,E,R,T,Y,U    - cause the ship to interpolate to a new orientation.
 *
 * @author integeruser
 */
public class Interpolation extends LWJGLWindow {
    public static void main(String[] args) {
        Framework.CURRENT_TUTORIAL_DATAPATH = "/fcagnin/jgltut/tut08/data/";

        new Interpolation().start();
    }


    @Override
    protected void init() {
        initializeProgram();

        try {
            ship = new Mesh( "Ship.xml" );
        } catch ( Exception exception ) {
            exception.printStackTrace();
            System.exit( -1 );
        }

        glEnable( GL_CULL_FACE );
        glCullFace( GL_BACK );
        glFrontFace( GL_CW );

        glEnable( GL_DEPTH_TEST );
        glDepthMask( true );
        glDepthFunc( GL_LEQUAL );
        glDepthRange( 0.0f, 1.0f );
    }

    @Override
    protected void display() {
        orient.updateTime();

        glClearColor( 0.0f, 0.0f, 0.0f, 0.0f );
        glClearDepth( 1.0f );
        glClear( GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT );

        MatrixStack currMatrix = new MatrixStack();
        currMatrix.translate( 0.0f, 0.0f, -200.0f );
        currMatrix.applyMatrix( Glm.matCast( orient.getOrient() ) );

        glUseProgram( theProgram );

        currMatrix.scale( 3.0f, 3.0f, 3.0f );
        currMatrix.rotateX( -90.0f );

        // Set the base color for this object.
        glUniform4f( baseColorUnif, 1.0f, 1.0f, 1.0f, 1.0f );
        glUniformMatrix4( modelToCameraMatrixUnif, false, currMatrix.top().fillAndFlipBuffer( mat4Buffer ) );

        ship.render( "tint" );

        glUseProgram( 0 );
    }

    @Override
    protected void reshape(int w, int h) {
        cameraToClipMatrix.set( 0, 0, frustumScale * (h / (float) w) );
        cameraToClipMatrix.set( 1, 1, frustumScale );

        glUseProgram( theProgram );
        glUniformMatrix4( cameraToClipMatrixUnif, false, cameraToClipMatrix.fillAndFlipBuffer( mat4Buffer ) );
        glUseProgram( 0 );

        glViewport( 0, 0, w, h );
    }

    @Override
    protected void update() {
        while ( Keyboard.next() ) {
            if ( Keyboard.getEventKeyState() ) {
                for ( int orientIndex = 0; orientIndex < orientKeys.length; orientIndex++ ) {
                    if ( Keyboard.getEventKey() == orientKeys[orientIndex] ) {
                        applyOrientation( orientIndex );
                        break;
                    }
                }

                switch ( Keyboard.getEventKey() ) {
                    case Keyboard.KEY_SPACE:
                        boolean slerp = orient.toggleSlerp();
                        System.out.printf( slerp ? "Slerp\n" : "Lerp\n" );
                        break;

                    case Keyboard.KEY_ESCAPE:
                        leaveMainLoop();
                        break;
                }
            }
        }
    }


    ////////////////////////////////
    private int theProgram;

    private int modelToCameraMatrixUnif;
    private int cameraToClipMatrixUnif;
    private int baseColorUnif;

    private Mat4 cameraToClipMatrix = new Mat4( 0.0f );
    private FloatBuffer mat4Buffer = BufferUtils.createFloatBuffer( Mat4.SIZE );

    private final float frustumScale = calcFrustumScale( 20.0f );


    private void initializeProgram() {
        ArrayList<Integer> shaderList = new ArrayList<>();
        shaderList.add( Framework.loadShader( GL_VERTEX_SHADER, "PosColorLocalTransform.vert" ) );
        shaderList.add( Framework.loadShader( GL_FRAGMENT_SHADER, "ColorMultUniform.frag" ) );

        theProgram = Framework.createProgram( shaderList );

        modelToCameraMatrixUnif = glGetUniformLocation( theProgram, "modelToCameraMatrix" );
        cameraToClipMatrixUnif = glGetUniformLocation( theProgram, "cameraToClipMatrix" );
        baseColorUnif = glGetUniformLocation( theProgram, "baseColor" );

        float zNear = 1.0f;
        float zFar = 600.0f;

        cameraToClipMatrix.set( 0, 0, frustumScale );
        cameraToClipMatrix.set( 1, 1, frustumScale );
        cameraToClipMatrix.set( 2, 2, (zFar + zNear) / (zNear - zFar) );
        cameraToClipMatrix.set( 2, 3, -1.0f );
        cameraToClipMatrix.set( 3, 2, (2 * zFar * zNear) / (zNear - zFar) );

        glUseProgram( theProgram );
        glUniformMatrix4( cameraToClipMatrixUnif, false, cameraToClipMatrix.fillAndFlipBuffer( mat4Buffer ) );
        glUseProgram( 0 );
    }


    private float calcFrustumScale(float fovDeg) {
        final float degToRad = 3.14159f * 2.0f / 360.0f;
        float fovRad = fovDeg * degToRad;
        return (float) (1.0f / Math.tan( fovRad / 2.0f ));
    }


    ////////////////////////////////
    private Mesh ship;

    private Quaternion orients[] = {
            new Quaternion( 0.7071f, 0.7071f, 0.0f, 0.0f ),
            new Quaternion( 0.5f, 0.5f, -0.5f, 0.5f ),
            new Quaternion( -0.4895f, -0.7892f, -0.3700f, -0.02514f ),
            new Quaternion( 0.4895f, 0.7892f, 0.3700f, 0.02514f ),

            new Quaternion( 0.3840f, -0.1591f, -0.7991f, -0.4344f ),
            new Quaternion( 0.5537f, 0.5208f, 0.6483f, 0.0410f ),
            new Quaternion( 0.0f, 0.0f, 1.0f, 0.0f )
    };

    private int orientKeys[] = {
            Keyboard.KEY_Q,
            Keyboard.KEY_W,
            Keyboard.KEY_E,
            Keyboard.KEY_R,

            Keyboard.KEY_T,
            Keyboard.KEY_Y,
            Keyboard.KEY_U
    };

    private Orientation orient = new Orientation();

    private class Orientation {
        boolean isAnimating;
        boolean slerp;
        int currOrientIndex;

        Animation anim = new Animation();

        class Animation {
            int finalOrientIndex;
            Timer currTimer;


            boolean updateTime() {
                return currTimer.update( getElapsedTime() );
            }

            void startAnimation(int destinationIndex, float duration) {
                finalOrientIndex = destinationIndex;
                currTimer = new Timer( Timer.Type.SINGLE, duration );
            }


            Quaternion getOrient(Quaternion initial, boolean slerp) {
                if ( slerp ) {
                    return slerp( initial, orients[finalOrientIndex], currTimer.getAlpha() );
                } else {
                    return lerp( initial, orients[finalOrientIndex], currTimer.getAlpha() );
                }
            }

            int getFinalIndex() {
                return finalOrientIndex;
            }
        }


        void updateTime() {
            if ( isAnimating ) {
                boolean isFinished = anim.updateTime();
                if ( isFinished ) {
                    isAnimating = false;
                    currOrientIndex = anim.getFinalIndex();
                }
            }
        }

        void animateToOrient(int destinationIndex) {
            if ( currOrientIndex == destinationIndex ) { return; }

            anim.startAnimation( destinationIndex, 1.0f );
            isAnimating = true;
        }


        boolean toggleSlerp() {
            slerp = !slerp;
            return slerp;
        }

        Quaternion getOrient() {
            if ( isAnimating ) {
                return anim.getOrient( orients[currOrientIndex], slerp );
            } else {
                return orients[currOrientIndex];
            }
        }

        boolean isAnimating() {
            return isAnimating;
        }
    }


    private void applyOrientation(int orientationIndex) {
        if ( !orient.isAnimating() ) {
            orient.animateToOrient( orientationIndex );
        }
    }


    private Quaternion slerp(Quaternion v0, Quaternion v1, float alpha) {
        float dot = Glm.dot( v0, v1 );

        final float DOT_THRESHOLD = 0.9995f;
        if ( dot > DOT_THRESHOLD ) {
            return lerp( v0, v1, alpha );
        }

        Glm.clamp( dot, -1.0f, 1.0f );
        float theta_0 = (float) Math.acos( dot );
        float theta = theta_0 * alpha;

        Quaternion v2 = Quaternion.add( v1, Quaternion.negate( Quaternion.scale( v0, dot ) ) );
        v2 = Glm.normalize( v2 );

        return Quaternion.add( Quaternion.scale( v0, (float) Math.cos( theta ) ),
                Quaternion.scale( v2, (float) Math.sin( theta ) ) );
    }

    private Quaternion lerp(Quaternion v0, Quaternion v1, float alpha) {
        Vec4 start = vectorize( v0 );
        Vec4 end = vectorize( v1 );
        Vec4 interp = Glm.mix( start, end, alpha );

        System.out.printf( "alpha: %f, (%f, %f, %f, %f)\n", alpha, interp.w, interp.x, interp.y, interp.z );

        interp = Glm.normalize( interp );
        return new Quaternion( interp.w, interp.x, interp.y, interp.z );
    }

    private Vec4 vectorize(Quaternion theQuat) {
        Vec4 vec = new Vec4();
        vec.x = theQuat.x;
        vec.y = theQuat.y;
        vec.z = theQuat.z;
        vec.w = theQuat.w;
        return vec;
    }
}