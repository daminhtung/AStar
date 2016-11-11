package com.example.framgianguyenvantung.astar;

import android.graphics.Point;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

import Ultis.Libs;
import View.Node;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private Node[][] map;
    private int maxRow = 12;
    private int maxColumn = 15;
    private LinearLayout mapLinear;
    private Node startPoint;
    private Node endPoint;
    private STATE state = STATE.NONE;

    //UI
    Button startBtn, endBtn;

    public enum STATE {
        SETUP_START_POINT, SETUP_END_POINT, SETUP_OBSTACLE, NONE
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
        startBtn = (Button)  findViewById(R.id.button_start);
        startBtn.setOnClickListener(this);
        endBtn = (Button) findViewById(R.id.button_end);
        endBtn.setOnClickListener(this);
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
                node.setPosition(new Point(j, i));
                if (i == 0 || i == maxRow - 1 || j == 0 || j == maxColumn - 1) {
                    node.setWalkable(false);
                } else {
                    node.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (state == STATE.SETUP_OBSTACLE) {
                                node.setWalkable(!node.isWalkable());
                            } else if (state == STATE.SETUP_START_POINT) {
                                startPoint = node;
                                startPoint.setBackgroundColor(getResources().getColor(R.color.colorWhite));
                            } else if (state == STATE.SETUP_END_POINT) {
                                endPoint = node;
                                startPoint.setBackgroundColor(getResources().getColor(R.color.colorWhite));
                            }
                        }
                    });
                }

                map[i][j] = node;
            }
            mapLinear.addView(rowIndexLL);
        }
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
                if (map[i][j].isWalkable() && state == STATE.NONE)
                    map[i][j].setBackgroundColor(getResources().getColor(R.color.colorPrimary));
            }
        }
    }

    private Stack<Node> AStarpathfinding(Node start, Node target) {
        if (target == null)
            return null;
        List<Node> Open = new ArrayList<>();
        List<Node> Close = new ArrayList<>();
        //Lam rong het Open va Close truoc
        Open.clear();
        Close.clear();
        //Tinh toan g(gia tri tu diem bat dau toi diem hien tai),h(gia tri tu diem hien tai doi dich),
        // f: gia tri tu diem bat dau dtoi diem dich di qua diem hien tai) cho diem start
        start.setG(0);
        start.setH(Libs.getInstance().distanceTwoPoint(start.getPosition(), target.getPosition()));
        start.setF(start.getG() + start.getH());

        //Them start vao Open list
        Open.add(start);

        while (Open.size() != 0) { // Chung nao Openlist chua xet het thi van xet tiep
            //Tim node co f nho nhat trong Open list
            Node currentNode = Open.get(0);
            for (Node node_i : Open) {
                if (node_i.getF() < currentNode.getF()) {
                    currentNode = node_i;
                }
            }
            //Sau khi tim duoc thang nho nhat trong Open list roi thi remove no di
            Open.remove(currentNode);
            //add no vao Close list
            Close.add(currentNode);
            //Neu diem hien tai = diem dich thi return path ngan nhat luon
            if (currentNode.getNodeID().equals(target.getNodeID())) {
                Open.clear();
                Close.clear();
                return ReconstructPath(start, target); //viet sau
            } else {
                //Xet voi cac node tiep theo lien ke
                for (Node nodeIndex : currentNode.getNext()) {
                    Node node_index = nodeIndex;
                    if (Close.contains(node_index)) {
                        //Xeta neu node lien ke voi node hien tai ma co trong Close roi thi thoi k can xet nua
                        continue;
                    }

                    double tmp_current_g = currentNode.getG() + Libs.getInstance().distanceTwoPoint(currentNode.getPosition(), node_index.getPosition());
                    if (!Open.contains(node_index) || tmp_current_g < node_index.getG()) {
                        /*Neu node lien ke cua node hien tai ma chua nam trong Openlist(danh sach cac node dang hoac se duoc xem xet)
                         * hoac khoang cach tu diem xuat phat toi diem lien ke di qua diem hien tai ma nho hon
						 * khoang cach tu diem xuat phat toi diem lien ke khong xet qua g cua diem hien tai thi cung duoc tinh toan
						 * */
                        node_index.setCaneFrom(currentNode);
                        node_index.setG(tmp_current_g);
                        node_index.setH(Libs.getInstance().distanceTwoPoint(node_index.getPosition(), target.getPosition()));
                        node_index.setF(node_index.getG() + node_index.getH());

                        //Neu chua co trong Open thi add
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
        switch (v.getId()) {
            case R.id.execute:
                clearAdjacent();
                if (state == STATE.NONE) return;
                setupNextNode();
                AStarpathfinding(startPoint, endPoint);
                break;
            case R.id.obstacle:
                clearAdjacent();
                if (state == STATE.SETUP_START_POINT || state == STATE.SETUP_END_POINT) return;
                state = state != STATE.SETUP_OBSTACLE ? STATE.SETUP_OBSTACLE : STATE.NONE;
                mapLinear.setBackgroundColor(getResources().getColor(state == STATE.SETUP_OBSTACLE ? R.color.colorAccent : R.color.colorWhite));
                break;
            case R.id.button_start:
                state = state == STATE.SETUP_START_POINT ? STATE.NONE : STATE.SETUP_START_POINT;
                startBtn.setText(state == STATE.SETUP_START_POINT ? "Setuping" : "Set Start");
                break;
            case R.id.button_end:
                state = state == STATE.SETUP_END_POINT ? STATE.NONE : STATE.SETUP_END_POINT;
                endBtn.setText(state == STATE.SETUP_END_POINT ? "Setuping" : "Set End");
                break;
        }
    }


    private Stack<Node> ReconstructPath(Node s, Node t) {
        Stack<Node> path = new Stack<Node>();
        path.clear();
        Node tmp = t;
        while (tmp != null) {
            if (tmp.getCaneFrom() == null) return path;
            path.push(tmp);
            tmp = tmp.getCaneFrom();
            tmp.setBackgroundColor(getResources().getColor(R.color.colorPathResult));
        }
        return path;
    }
}
