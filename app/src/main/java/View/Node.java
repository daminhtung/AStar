package View;

import android.content.Context;
import android.os.Debug;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.example.framgianguyenvantung.astar.MainActivity;
import com.example.framgianguyenvantung.astar.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by FRAMGIA\nguyen.van.tung on 10/11/2016.
 */
public class Node extends ImageView{

    private String nodeID = "";
    private int position;
    private List<Node> next = new ArrayList<>();
    private  Node caneFrom;
    private  boolean isWalkable = true;
    private int G;
    private int H;
    private int F;
    private MainActivity parentClass;

    public Node(Context context) {
        super(context);
        setBackgroundColor(getResources().getColor(R.color.colorPrimary));
        parentClass = (MainActivity)context;
    }

    @Override
    public void setBackgroundColor(int color) {
        super.setBackgroundColor(color);
    }

    public String getNodeID() {
        return nodeID;
    }

    public void setNodeID(String id) {
        this.nodeID = id;
    }

    public Node getCaneFrom() {
        return caneFrom;
    }

    public void setCaneFrom(Node caneFrom) {
        this.caneFrom = caneFrom;
    }

    public int getF() {
        return F;
    }

    public void setF(int f) {
        F = f;
    }

    public int getG() {
        return G;
    }

    public void setG(int g) {
        G = g;
    }

    public int getH() {
        return H;
    }

    public void setH(int h) {
        H = h;
    }

    public boolean isWalkable() {
        return isWalkable;
    }

    public void setWalkable(boolean walkable) {
        isWalkable = walkable;
    }

    public List<Node> getNext() {
        return next;
    }

    public void setNext(List<Node> next) {
        this.next = next;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }
}
