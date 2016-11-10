package com.example.framgianguyenvantung.astar;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import Ultis.Libs;
import View.Node;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private Node[][] map;
    private int maxRow = 12;
    private int maxColumn = 15;
    private LinearLayout mapLinear;
    private boolean makeObstack = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initialMap();
        findView();
    }

    private  void findView(){
        findViewById(R.id.execute).setOnClickListener(this);
        findViewById(R.id.obstacle).setOnClickListener(this);
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
                node.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Log.i("A*","OnClick to Node");
                        if(isMakeObstack()){
                            node.setWalkable(!node.isWalkable());
                        }
                        node.setBackgroundColor(getResources().getColor(node.isWalkable() ? R.color.colorPrimary : R.color.colorBlack));
                    }
                });
                map[i][j] = node;
            }
            mapLinear.addView(rowIndexLL);
        }
    }

    public boolean isMakeObstack() {
        return makeObstack;
    }

    public void setMakeObstack(boolean makeObstack) {
        this.makeObstack = makeObstack;
    }

    private void AStarpathfinding() {

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.execute:
                AStarpathfinding();
                break;
            case R.id.obstacle:
                setMakeObstack(!makeObstack);
                mapLinear.setBackgroundColor(getResources().getColor(makeObstack ?  R.color.colorAccent : R.color.colorWhite));
                break;
        }
    }
}
