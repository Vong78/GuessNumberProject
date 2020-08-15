package com.example.GuessNumberApp;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private ImageButton[] ibInputs = new ImageButton[10];
    private int[] btnInputIDS = {R.id.ib_num0, R.id.ib_num1, R.id.ib_num2, R.id.ib_num3, R.id.ib_num4, R.id.ib_num5, R.id.ib_num6, R.id.ib_num7, R.id.ib_num8, R.id.ib_num9};

    private View[] ibInputFuction = new View[4];
    private int[] btnInputFunctionIDS = {R.id.ib_numback, R.id.ib_numclear, R.id.ib_send, R.id.ib_replay};

    private TextView[] tvShows = new TextView[4];
    private int[] tvShowIDS = {R.id.tv_num1, R.id.tv_num2, R.id.tv_num3, R.id.tv_num4};

    private ImageView ivGradePic;

    private TextView tvTimerSec, tvTimerMin;

    private LinkedList<Integer> answers = new LinkedList<>(); // 產生的隨機答案
    private int inputPoint = 0;
    private LinkedList<Integer> inputValue = new LinkedList<>();   // 輸入數值陣列

    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private LinkedList<HashMap<String, String>> hist;
    private ViewAdapter adapter;
    private String[] from = {"order", "guess", "result"};
    private int No = 1;

    private int sec = 0, min = 0;
    private Timer timer = null;

    private int RoundGame = 1000;

    private boolean finishBtn = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        initGame();
        initRecycleView();
    }

    private void initRecycleView() {
        recyclerView = findViewById(R.id.rv);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        hist = new LinkedList<>();
        adapter = new ViewAdapter(hist, MainActivity.this);
        recyclerView.setAdapter(adapter);
    }

    private void initView() {
        for (int i = 0; i < btnInputIDS.length; i++) {
            ibInputs[i] = findViewById(btnInputIDS[i]);
        }
        for (int j = 0; j < tvShowIDS.length; j++) {
            tvShows[j] = findViewById(tvShowIDS[j]);
        }
        for (int k = 0; k < btnInputFunctionIDS.length; k++) {
            ibInputFuction[k] = findViewById(btnInputFunctionIDS[k]);
            ibInputFuction[k].setOnClickListener(this);
        }
        tvTimerMin = findViewById(R.id.tv_timermin);
        tvTimerSec = findViewById(R.id.tv_timersec);
        ivGradePic = findViewById(R.id.iv_viewgrade);
    }

    private void initGame() {
        RoundGame = 1000;

        final LayoutInflater inflater = LayoutInflater.from(this);

        View view2 = inflater.inflate(R.layout.gamemode, null);

        final AlertDialog.Builder b2 = new AlertDialog.Builder(this);
        b2.setView(view2);
        Button btnTimerGame = view2.findViewById(R.id.btn_timergame);
        Button btnRoundGame = view2.findViewById(R.id.btn_roundgame);
        final AlertDialog dia = b2.setCancelable(false).show();

        btnTimerGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dia.dismiss();

                final View view = inflater.inflate(R.layout.timergamelevel, null);
                AlertDialog.Builder b1 = new AlertDialog.Builder(MainActivity.this);
                b1.setTitle("猜數字遊戲:");
                b1.setMessage("請選擇遊戲難易度，並確定開始～");
                //--------------
                b1.setView(view);
                final Button btnEasy = view.findViewById(R.id.btn_easy);
                final Button btnMedium = view.findViewById(R.id.btn_medium);
                final Button btnHard = view.findViewById(R.id.btn_hard);

                btnEasy.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        btnEasy.setAlpha(0.5f);
                        btnHard.setAlpha(1.0f);
                        btnMedium.setAlpha(1.0f);
                        toastMessage("容易");
                        min = 4;
                        sec = 60;
                    }
                });

                btnMedium.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        btnEasy.setAlpha(1.0f);
                        btnHard.setAlpha(1.0f);
                        btnMedium.setAlpha(0.5f);
                        toastMessage("中等");
                        min = 3;
                        sec = 60;
                    }
                });

                btnHard.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        btnEasy.setAlpha(1.0f);
                        btnHard.setAlpha(0.5f);
                        btnMedium.setAlpha(1.0f);
                        toastMessage("困難");
                        min = 2;
                        sec = 60;
                    }
                });
                //--------------
                b1.setPositiveButton("確定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        TimerGameLevelStart();
                    }
                });
                //-----------------
                b1.setNeutralButton("返回", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        initGame();
                    }
                }).setCancelable(false).show();

            }
        });

        btnRoundGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //
                dia.dismiss();

                final View view = inflater.inflate(R.layout.roundgamelevel, null);
                AlertDialog.Builder b3 = new AlertDialog.Builder(MainActivity.this);
                b3.setTitle("猜數字遊戲:");
                b3.setMessage("請選擇遊戲難易度，並確定開始～");
                //--------------
                b3.setView(view);
                final Button btnEasy30 = view.findViewById(R.id.btn_easy);
                final Button btnMedium20 = view.findViewById(R.id.btn_medium);
                final Button btnHard10 = view.findViewById(R.id.btn_hard);

                btnEasy30.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        btnEasy30.setAlpha(0.5f);
                        btnHard10.setAlpha(1.0f);
                        btnMedium20.setAlpha(1.0f);
                        toastMessage("容易");
                        RoundGame = 30;
                    }
                });

                btnMedium20.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        btnEasy30.setAlpha(1.0f);
                        btnHard10.setAlpha(1.0f);
                        btnMedium20.setAlpha(0.5f);
                        toastMessage("中等");
                        RoundGame = 20;

                    }
                });

                btnHard10.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        btnEasy30.setAlpha(1.0f);
                        btnHard10.setAlpha(0.5f);
                        btnMedium20.setAlpha(1.0f);
                        toastMessage("困難");
                        RoundGame = 10;
                    }
                });
                //--------------
                b3.setPositiveButton("確定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if(RoundGame == 1000){
                            initGame();
                            toastMessage("請先選者需要的挑戰方式 及 難易度");
                            return;
                        }
                        ivGradePic.setVisibility(View.INVISIBLE);
                        No = 1;
                        answers = createAnswer();
                        Log.d("answer", answers + "");
                        clear(null);
                    }
                });
                //-----------------
                b3.setNeutralButton("返回", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        initGame();
                    }
                }).setCancelable(false).show();

                //
            }
        });


    }

    private void TimerGameLevelStart() {
        if (min != 0 && sec != 0 && timer == null) {
            ivGradePic.setVisibility(View.INVISIBLE);
            No = 1;
            answers = createAnswer();
            Log.d("answer", answers + "");
            clear(null);

            timer = new Timer();
            final TimerTask task = new TimerTask() {
                @Override
                public void run() {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            sec--;
                            tvTimerSec.setText(String.format("%02d", sec));
                            tvTimerMin.setText(String.format("%02d", min));

                            if (min == 0 && sec == 0) {
                                timer.cancel();
                                sec = 0;
                                min = 0;
                                tvTimerSec.setText(String.format("%02d", sec));
                                tvTimerMin.setText(String.format("%02d", min));
                                disPlayResult(false);
                            } else if (min != 0 && sec == 0) {
                                sec = 60;
                                min--;
                            }

                        }
                    });
                }
            };
            //時間在幾毫秒過後開始以多少毫秒執行
            timer.schedule(task, 1000, 1000);
        } else {
            toastMessage("請先選者需要的挑戰方式 及 難易度");
            initGame();
        }
    }

    private void toastMessage(String s) {
        Toast.makeText(this, s, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ib_replay:
                replay(v);
                break;
            case R.id.ib_numback:
                back(v);
                break;
            case R.id.ib_numclear:
                clear(v);
                break;
            case R.id.ib_send:
                send(v);
                break;
        }
    }

    public void inputType(View view) {
        if (inputPoint == 4) {
            return;
        }
        for (int i = 0; i < ibInputs.length; i++) {
            if (view == ibInputs[i]) {
                inputValue.set(inputPoint, i);
                tvShows[inputPoint].setText("" + i);
                inputPoint++;
                ibInputs[i].setEnabled(false);
                ibInputs[i].setAlpha(0.5f);
                break;
            }
        }
    }

    private void send(View v) {
        //顯示猜次數遊戲猜的次數
        if (RoundGame != 1000) {
            toastMessage("第" + (hist.size()+1) + "次");
        }

        if (inputPoint != 4) {
            return;
        }

        int a, b;
        a = b = 0;
        String guess = "";
        for (int i = 0; i < inputValue.size(); i++) {
            guess += inputValue.get(i);
            if (inputValue.get(i).equals(answers.get(i))) {
                a++;
                //只要答案裡的數字有和輸入相撞，就b++
            } else if (answers.contains(inputValue.get(i))) {
                b++;
            }
        }
        Log.d("grade", a + "A" + b + "B");
        clear(null);

        HashMap<String, String> row = new HashMap<>();
        row.put(from[0], String.format("%02d", No));
        row.put(from[1], guess);
        row.put(from[2], a + "A" + b + "B");
        hist.add(row);
        adapter.notifyDataSetChanged();
        recyclerView.smoothScrollToPosition(hist.size() - 1);
        No++;

        if (a == 4) {
            disPlayResult(true);
            //遊戲猜的次數
        } else if (hist.size() == RoundGame) {
            disPlayResult(false);
        }
    }

    private void disPlayResult(boolean b) {
        ivGradePic.setVisibility(View.VISIBLE);
        if (b) {
            ivGradePic.setImageResource(R.drawable.winner);
            if (RoundGame == 1000) {
                timer.cancel();
            }
        } else {
            ivGradePic.setImageResource(R.drawable.loser);
            if (RoundGame == 1000) {
                timer.cancel();
            }

        }
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(false);
        builder.setTitle("遊戲結果");

        StringBuffer ansString = new StringBuffer();
        for (int i = 0; i < answers.size(); i++) ansString.append(answers.get(i));

        builder.setMessage(b ? "完全正確" : "挑戰失敗\n" + "答案:" + ansString);
        builder.setPositiveButton("開新局", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                replay(null);
            }
        });
        builder.setNeutralButton("結束遊戲", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        });
        builder.create().show();
    }

    public void replay(View view) {
        sec = 0;
        min = 0;
        tvTimerSec.setText(String.format("%02d", sec));
        tvTimerMin.setText(String.format("%02d", min));
        if (RoundGame == 1000) {
            timer.cancel();
            timer = null;
        }
        RoundGame = 1000;

        initGame();
        hist.clear();
        adapter.notifyDataSetChanged();
    }

    private void clear(View view) {
        inputPoint = 0;
        inputValue.clear();
        for (int i = 0; i < 4; i++) {
            inputValue.add(-1);
        }
        for (int i = 0; i < tvShows.length; i++) {
            tvShows[i].setText("");
        }
        for (int i = 0; i < ibInputs.length; i++) {
            ibInputs[i].setAlpha(1.0f);
            ibInputs[i].setEnabled(true);
        }
    }

    private void back(View v) {
        if (inputPoint == 0) {
            return;
        }
        inputPoint--;
        ibInputs[inputValue.get(inputPoint)].setEnabled(true);
        ibInputs[inputValue.get(inputPoint)].setAlpha(1.0f);
        inputValue.set(inputPoint, -1);
        tvShows[inputPoint].setText("");
    }

    private LinkedList<Integer> createAnswer() {
        LinkedList<Integer> ret = new LinkedList<>();
        HashSet<Integer> nums = new HashSet<>();
        while (nums.size() < 4) {
            nums.add((int) (Math.random() * 10));
        }
        for (Integer i : nums) {
            ret.add(i);
        }
        //洗牌的動作
        Collections.shuffle(ret);
        return ret;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode == KeyEvent.KEYCODE_BACK){
            if(finishBtn) {
                finish();
            }else {
                finishBtn=true;
                toastMessage("再點擊一次遊戲將退出並結束");
                return false;
            }
        }
        return super.onKeyDown(keyCode, event);
    }

}