package com.example.framgianguyenvantung.astar;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import Ultis.Libs;
import View.Node;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private final String START_POINT = "Start Point";
    private final String END_POINT = "End Point";
    private final String OBSTACLE_POINT = "Obstacle";
    private final String OPEN_POINT = "Open";
    private String typeNode = OPEN_POINT;
    private Node[][] map;
    private int maxRow = 12;
    private int maxColumn = 15;
    private LinearLayout mapLinear;
    private Node startPoint;
    private Node endPoint;
    private STATE state = STATE.NONE;

    private TextView distanceTv;
    public enum STATE {
        SETUP_MAP, NONE
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initialMap();
        findView();
    }

    private void findView() {
        findViewById(R.id.execute).setOnClickListener(this);
        findViewById(R.id.obstacle).setOnClickListener(this);
        distanceTv = (TextView)findViewById(R.id.distance);
    }

    private void initialMap() {
        map = new Node[maxRow][maxColumn];
        mapLinear = (LinearLayout) findViewById(R.id.map_wrap);
        int nodeSize = Libs.getInstance().convertDpToPixel(20, this);

        for (int i = 0; i < maxRow; i++) {
            LinearLayout rowIndexLL = new LinearLayout(this);
            rowIndexLL.setLayoutParams(new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT));

            for (int j = 0; j < maxColumn; j++) {
                final Node node = new Node(this);
                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(nodeSize, nodeSize);
                layoutParams.setMargins(1, 1, 0, 0);
                node.setLayoutParams(layoutParams);
                rowIndexLL.addView(node);
                node.setNodeID(i + "" + j);
//                node.setPosition(new Point(j, i));
                if (i == 0 || i == maxRow - 1 || j == 0 || j == maxColumn - 1) {
                    node.setWalkable(false);
                } else {
                    node.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (state != STATE.SETUP_MAP) return;
                            setupNodeType(node);
                        }
                    });
                }
                map[i][j] = node;
            }
            mapLinear.addView(rowIndexLL);
        }
    }

    private void setNodeState(String type, Node node) {
        switch (type) {
            case START_POINT:
                if (endPoint != null && node.getNodeID().equals(endPoint.getNodeID())) return;
                if (startPoint != null) {
                    startPoint.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                }
                startPoint = node;
                startPoint.setBackgroundColor(getResources().getColor(R.color.colorWhite));
                break;
            case END_POINT:
                if (startPoint != null && node.getNodeID().equals(startPoint.getNodeID())) return;
                if (endPoint != null) {
                    endPoint.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                }
                endPoint = node;
                endPoint.setBackgroundColor(getResources().getColor(R.color.colorWhite));
                break;
            case OBSTACLE_POINT:
                node.setWalkable(false);
                break;
            case OPEN_POINT:
                node.setWalkable(true);
                break;
        }
    }

    private void setupNodeType(final Node node) {
        final CharSequence types[] = new CharSequence[]{OBSTACLE_POINT, START_POINT, END_POINT};
        final AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setItems(types, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                typeNode = types[which].toString();
                setNodeState(typeNode, node);
            }
        });
        builder.show();
    }

    private void setupNextNode() {
        for (int i = 1; i < maxRow - 1; i++) {
            for (int j = 1; j < maxColumn - 1; j++) {

                if (map[i - 1][j].isWalkable()) {
                    map[i][j].getNext().add(map[i - 1][j]);
                }

                if (map[i + 1][j].isWalkable()) {
                    map[i][j].getNext().add(map[i + 1][j]);
                }

                if (map[i][j - 1].isWalkable()) {
                    map[i][j].getNext().add(map[i][j - 1]);
                }

                if (map[i][j + 1].isWalkable()) {
                    map[i][j].getNext().add(map[i][j + 1]);
                }
            }
        }
    }

    private void clearAdjacent() {
        for (int i = 1; i < maxRow - 1; i++) {
            for (int j = 1; j < maxColumn - 1; j++) {
                map[i][j].getNext().clear();
                if (map[i][j].isWalkable() && state == STATE.NONE) {
                    if (startPoint != null && endPoint != null) {
                        if (map[i][j].getNodeID().equals(startPoint.getNodeID())
                                || map[i][j].getNodeID().equals(endPoint.getNodeID()))
                            continue;

                    }
                    map[i][j].setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                }
            }
        }
    }

    private List<Node> AStarpathfinding(Node start, Node target) {
        if (target == null)
            return null;
        List<Node> Open = new ArrayList<>();
        List<Node> Close = new ArrayList<>();
        Open.clear();
        Close.clear();
        start.setG(0);
        start.setH(Libs.getInstance().distanceTwoPoint1(start.getPosition(), target.getPosition()));
        start.setF(start.getG() + start.getH());

        Open.add(start);

        while (Open.size() != 0) {
            Node currentNode = Open.get(0);
            for (Node node_i : Open) {
                if (node_i.getF() < currentNode.getF()) {
                    currentNode = node_i;
                }
            }
            Open.remove(currentNode);
            Close.add(currentNode);
            if (currentNode.getNodeID().equals(target.getNodeID())) {
                Open.clear();
                Close.clear();
                return ReconstructPath(target); //viet sau
            } else {
                for (Node nodeIndex : currentNode.getNext()) {
                    Node node_index = nodeIndex;
                    if (Close.contains(node_index)) {
                        continue;
                    }

                    double tmp_current_g = currentNode.getG() + Libs.getInstance().distanceTwoPoint1(currentNode.getPosition(), node_index.getPosition());
                    if (!Open.contains(node_index) || tmp_current_g < node_index.getG()) {
                        node_index.setCaneFrom(currentNode);
                        node_index.setG(tmp_current_g);
                        node_index.setH(Libs.getInstance().distanceTwoPoint1(node_index.getPosition(), target.getPosition()));
                        node_index.setF(node_index.getG() + node_index.getH());

                        if (!Open.contains(node_index)) {
                            Open.add(node_index);
                        }
                    }
                }

            }
        }
        return null;
    }


    @Override
    public void onClick(View v) {
        clearAdjacent();
        switch (v.getId()) {
            case R.id.execute:
                if (state == STATE.SETUP_MAP || startPoint == null || endPoint == null) return;
                clearAdjacent();
                setupNextNode();
                startPoint = map[1][1];
                endPoint = map[5][8];
                AStarpathfinding(startPoint, endPoint);
                break;
            case R.id.obstacle:
                state = state != STATE.SETUP_MAP ? STATE.SETUP_MAP : STATE.NONE;
                mapLinear.setBackgroundColor(getResources().getColor(state == STATE.SETUP_MAP ? R.color.colorAccent : R.color.colorWhite));
                break;
        }
    }

    private List<Node> ReconstructPath(Node t) {
        List<Node> path = new ArrayList<>();
        path.clear();
        Node tmp = t;
        while (tmp != null) {
            if (tmp.getCaneFrom() == null){
                computeCostPath(path);
                return path;
            }
            path.add(tmp);
            tmp = tmp.getCaneFrom();
            if (!tmp.getNodeID().equals(startPoint.getNodeID()) && !tmp.getNodeID().equals(t.getNodeID()))
                tmp.setBackgroundColor(getResources().getColor(R.color.colorPathResult));
        }
        computeCostPath(path);
        return path;
    }

    private void computeCostPath(List<Node> nodes){
        double distance = 0;
        Log.i("TungDominico", "Before: "+distance);
        for (int i = 0; i < nodes.size() - 1; i++){
//            distance +=  Libs.getInstance().distanceTwoPoint(nodes.get(i).getPosition(), nodes.get(i+1).getCaneFrom().getPosition());
            Log.i("TungDominico",nodes.get(i).getNodeID());
        }
        Log.i("TungDominico", "After: "+distance+"");
        distanceTv.setText("Distance: " + distance);
    }
}
