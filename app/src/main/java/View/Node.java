package View;

import android.content.Context;
import android.graphics.Point;
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
    private Point position;
    private List<Node> next = new ArrayList<>();
    private  Node caneFrom;
    private  boolean isWalkable = true;
    private double G;
    private double H;
    private double F;
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

    public double getF() {
        return F;
    }

    public void setF(double f) {
        F = f;
    }

    public double getG() {
        return G;
    }

    public void setG(double g) {
        G = g;
    }

    public double getH() {
        return H;
    }

    public void setH(double h) {
        H = h;
    }

    public boolean isWalkable() {
        return isWalkable;
    }

    public void setWalkable(boolean walkable) {
        setBackgroundColor(getResources().getColor(walkable ? R.color.colorPrimary : R.color.colorBlack));
        isWalkable = walkable;
    }

    public List<Node> getNext() {
        return next;
    }

    public void setNext(List<Node> next) {
        this.next = next;
    }

    public Point getPosition() {
        return position;
    }

    public void setPosition(Point position) {
        this.position = position;
    }
}
