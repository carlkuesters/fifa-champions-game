package com.carlkuesters.fifachampions;

import java.nio.FloatBuffer;
import java.util.LinkedList;
import java.util.logging.Level;
import java.util.logging.Logger;
import com.jme3.animation.SkeletonControl;
import com.jme3.bounding.BoundingBox;
import com.jme3.math.FastMath;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;
import com.jme3.scene.*;

public class JMonkeyUtil{
    
    public static void disableLogger(){
        Logger.getLogger("").setLevel(Level.SEVERE);
        Logger.getLogger(SkeletonControl.class.getName()).setLevel(Level.SEVERE);
    }
    
    public static Vector3f getSpatialDimension(Spatial spatial){
        if(spatial.getWorldBound() instanceof BoundingBox){
            BoundingBox boundingBox = (BoundingBox) spatial.getWorldBound();
            return new Vector3f(2 * boundingBox.getXExtent(), 2 * boundingBox.getYExtent(), 2 * boundingBox.getZExtent());
        }
        return new Vector3f(0, 0, 0);
    }
    
    public static LinkedList<Geometry> getAllGeometryChilds(Spatial spatial){
        LinkedList<Geometry> geometryChilds = new LinkedList<Geometry>();
        if(spatial instanceof Node){
            Node node = (Node) spatial;
            for(int i=0;i<node.getChildren().size();i++){
                Spatial child = node.getChild(i);
                if(child instanceof Geometry){
                    Geometry geometry = (Geometry) child;
                    geometryChilds.add(geometry);
                }
                else{
                    geometryChilds.addAll(getAllGeometryChilds(child));
                }
            }
        }
        return geometryChilds;
    }

    public static void shiftAndScaleTextureCoordinates(Mesh mesh, Vector2f shift, Vector2f scale) {
        VertexBuffer textCoordBuffer = mesh.getBuffer(VertexBuffer.Type.TexCoord);
        FloatBuffer data = (FloatBuffer) textCoordBuffer.getData();
        data.clear();
        for (int i = 0; i < (data.limit() / 2); i++) {
            float x = data.get();
            float y = data.get();
            data.position(data.position() - 2);
            x = shift.getX() + (x * scale.getX());
            y = shift.getY() + (y * scale.getY());
            data.put(x).put(y);
        }
        data.clear();
        textCoordBuffer.updateData(data);
    }

    public static Quaternion getQuaternion_X(float degrees){
        return getQuaternion(degrees, Vector3f.UNIT_X);
    }
    
    public static Quaternion getQuaternion_Y(float degrees){
        return getQuaternion(degrees, Vector3f.UNIT_Y);
    }
    
    public static Quaternion getQuaternion_Z(float degrees){
        return getQuaternion(degrees, Vector3f.UNIT_Z);
    }
    
    public static Quaternion getQuaternion(float degrees, Vector3f axis){
        return new Quaternion().fromAngleAxis(((degrees / 360) * (2 * FastMath.PI)), axis);
    }
}
