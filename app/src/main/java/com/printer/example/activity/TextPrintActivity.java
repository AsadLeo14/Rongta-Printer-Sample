package com.printer.example.activity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.printer.example.R;
import com.printer.example.app.BaseActivity;
import com.printer.example.app.BaseApplication;
import com.printer.example.utils.BaseEnum;
import com.rt.printerlibrary.bean.LableSizeBean;
import com.rt.printerlibrary.bean.Position;
import com.rt.printerlibrary.cmd.Cmd;
import com.rt.printerlibrary.cmd.CpclFactory;
import com.rt.printerlibrary.cmd.EscFactory;
import com.rt.printerlibrary.cmd.PinFactory;
import com.rt.printerlibrary.cmd.TscFactory;
import com.rt.printerlibrary.cmd.ZplFactory;
import com.rt.printerlibrary.enumerate.CommonEnum;
import com.rt.printerlibrary.enumerate.CpclFontTypeEnum;
import com.rt.printerlibrary.enumerate.ESCFontTypeEnum;
import com.rt.printerlibrary.enumerate.PrintDirection;
import com.rt.printerlibrary.enumerate.PrintRotation;
import com.rt.printerlibrary.enumerate.SettingEnum;
import com.rt.printerlibrary.enumerate.TscFontTypeEnum;
import com.rt.printerlibrary.enumerate.ZplFontTypeEnum;
import com.rt.printerlibrary.exception.SdkException;
import com.rt.printerlibrary.factory.cmd.CmdFactory;
import com.rt.printerlibrary.printer.RTPrinter;
import com.rt.printerlibrary.setting.BitmapSetting;
import com.rt.printerlibrary.setting.CommonSetting;
import com.rt.printerlibrary.setting.TextSetting;

import java.io.UnsupportedEncodingException;

public class TextPrintActivity extends BaseActivity implements View.OnClickListener {

    private EditText et_text;
    private Button btn_txtprint;

    private RTPrinter rtPrinter;
    private String printStr;
    private Bitmap bmp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_text_print);
        initView();
        addListener();
        init();
    }

    @Override
    public void initView() {
        et_text = findViewById(R.id.et_text);
        btn_txtprint = findViewById(R.id.btn_txtprint);
    }

    @Override
    public void addListener() {
        btn_txtprint.setOnClickListener(this);
    }

    @Override
    public void init() {
        rtPrinter = BaseApplication.getInstance().getRtPrinter();
    }

    private void textPrint() throws UnsupportedEncodingException {
        printStr = et_text.getText().toString();

        if (TextUtils.isEmpty(printStr)) {
            printStr = "Hello Printer";
        }

        switch (BaseApplication.getInstance().getCurrentCmdType()) {
            case BaseEnum.CMD_PIN:
                pinTextPrint();
                break;
            case BaseEnum.CMD_ESC:
                escPrint();
//                escPrintIndiaTest();
                break;
            case BaseEnum.CMD_CPCL:
                cpclPrint();
                break;
            case BaseEnum.CMD_TSC:
                tscPrint();
                break;
            case BaseEnum.CMD_ZPL:
                zplTextPrint();
                break;
            default:
                break;
        }
    }

    private void tscPrint() throws UnsupportedEncodingException {
        if (rtPrinter == null) {
            return;
        }
        CmdFactory tscFac = new TscFactory();
        Cmd tscCmd = tscFac.create();

        tscCmd.append(tscCmd.getHeaderCmd());
        CommonSetting commonSetting = new CommonSetting();
        commonSetting.setLableSizeBean(new LableSizeBean(80, 40));
        commonSetting.setLabelGap(3);
        commonSetting.setPrintDirection(PrintDirection.NORMAL);
        tscCmd.append(tscCmd.getHeaderCmd());
        tscCmd.append(tscCmd.getCommonSettingCmd(commonSetting));

        TextSetting textSetting = new TextSetting();
        textSetting.setTscFontTypeEnum(TscFontTypeEnum.Font_TSS24_BF2_For_Simple_Chinese);

        int x = 10;
        int y = 10;
        Position position = new Position(x, y);


        textSetting.setTxtPrintPosition(position);
        textSetting.setPrintRotation(PrintRotation.Rotate0);
        textSetting.setxMultiplication(1);
        textSetting.setyMultiplication(1);
        tscCmd.append(tscCmd.getTextCmd(textSetting, printStr));

        position.x = 100;
        position.y = 40;
        textSetting.setTxtPrintPosition(position);
        textSetting.setPrintRotation(PrintRotation.Rotate90);
        textSetting.setxMultiplication(2);
        textSetting.setyMultiplication(2);
        tscCmd.append(tscCmd.getTextCmd(textSetting, printStr));

        position.x = 400;
        position.y = 80;
        textSetting.setTxtPrintPosition(position);
        textSetting.setPrintRotation(PrintRotation.Rotate180);
        textSetting.setxMultiplication(1);
        textSetting.setyMultiplication(2);
        tscCmd.append(tscCmd.getTextCmd(textSetting, printStr));

        position.x = 200;
        position.y = 40 * 8 - 20;
        textSetting.setTxtPrintPosition(position);
        textSetting.setPrintRotation(PrintRotation.Rotate270);
        textSetting.setxMultiplication(1);
        textSetting.setyMultiplication(2);
        tscCmd.append(tscCmd.getTextCmd(textSetting, printStr));

        try {
            tscCmd.append(tscCmd.getPrintCopies(1));
        } catch (SdkException e) {
            e.printStackTrace();
        }
        tscCmd.append(tscCmd.getEndCmd());
        rtPrinter.writeMsgAsync(tscCmd.getAppendCmds());
    }

    private void cpclPrint() {
        if (rtPrinter == null) {
            return;
        }
        CmdFactory cpclFac = new CpclFactory();
        Cmd cmd = cpclFac.create();

        cmd.append(cmd.getCpclHeaderCmd(80, 60, 1));

        TextSetting textSetting = new TextSetting();
        textSetting.setCpclFontTypeEnum(CpclFontTypeEnum.Font_2);
        textSetting.setTxtPrintPosition(new Position(80, 80));
        textSetting.setPrintRotation(PrintRotation.Rotate0);
        textSetting.setxMultiplication(1);
        textSetting.setyMultiplication(1);
        try {
            cmd.append(cmd.getTextCmd(textSetting, printStr));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        cmd.append(cmd.getEndCmd());
        rtPrinter.writeMsgAsync(cmd.getAppendCmds());
    }

    private void escPrint() throws UnsupportedEncodingException {
        if (rtPrinter != null) {
            CmdFactory escFac = new EscFactory();
            Cmd escCmd = escFac.create();
            escCmd.append(escCmd.getHeaderCmd());//初始化, Initial

            escCmd.setChartsetName("UTF-8");

            TextSetting textSetting = new TextSetting();
            textSetting.setAlign(CommonEnum.ALIGN_MIDDLE);//对齐方式-左对齐，居中，右对齐
            textSetting.setBold(SettingEnum.Disable);
            textSetting.setUnderline(SettingEnum.Disable);
            textSetting.setIsAntiWhite(SettingEnum.Disable);
            textSetting.setDoubleHeight(SettingEnum.Disable);
            textSetting.setDoubleWidth(SettingEnum.Disable);

            textSetting.setEscFontType(ESCFontTypeEnum.FONT_A_12x24);

            escCmd.append(escCmd.getTextCmd(textSetting, printStr));

            escCmd.append(escCmd.getLFCRCmd());
            escCmd.append(escCmd.getLFCRCmd());
            escCmd.append(escCmd.getLFCRCmd());
            escCmd.append(escCmd.getLFCRCmd());
            escCmd.append(escCmd.getLFCRCmd());
            escCmd.append(escCmd.getHeaderCmd());//初始化, Initial
            escCmd.append(escCmd.getLFCRCmd());

            rtPrinter.writeMsgAsync(escCmd.getAppendCmds());
        }
    }

    private void escPrintIndiaTest() throws UnsupportedEncodingException {
        new Thread(new Runnable() {
            @Override
            public void run() {
                if (rtPrinter != null) {
                    CmdFactory escFac = new EscFactory();
                    Cmd escCmd = escFac.create();
                    escCmd.setChartsetName("UTF-8");
                    escCmd.append(escCmd.getHeaderCmd());//初始化, Initial

                    CommonSetting commonSetting = new CommonSetting();
                    commonSetting.setAlign(CommonEnum.ALIGN_MIDDLE);
                    escCmd.append(escCmd.getCommonSettingCmd(commonSetting));

                    BitmapSetting bitmapSetting = new BitmapSetting();
                    bitmapSetting.setBimtapLimitWidth(48*8);//限制图片最大宽度 58打印机=48mm， 80打印机=72mm
                    if(bmp == null){
                        bmp = BitmapFactory.decodeResource(getResources(), R.drawable.bill_bmptest);
                    }
                    try {
                        escCmd.append(escCmd.getBitmapCmd(bitmapSetting, Bitmap.createBitmap(bmp)));
                    } catch (SdkException e) {
                        e.printStackTrace();
                    }
                    escCmd.append(escCmd.getLFCRCmd());

                    TextSetting textSetting = new TextSetting();
                    textSetting.setAlign(CommonEnum.ALIGN_MIDDLE);//对齐方式-左对齐，居中，右对齐
                    try {
                        escCmd.append(escCmd.getTextCmd(textSetting, printStr));
                        escCmd.append(escCmd.getLFCRCmd());
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                    escCmd.append(escCmd.getLFCRCmd());
                    escCmd.append(escCmd.getLFCRCmd());
                    escCmd.append(escCmd.getLFCRCmd());
                    escCmd.append(escCmd.getLFCRCmd());
                    escCmd.append(escCmd.getHeaderCmd());//初始化, Initial
                    escCmd.append(escCmd.getLFCRCmd());

                    rtPrinter.writeMsgAsync(escCmd.getAppendCmds());
                }
            }
        }).start();

    }

    private void pinTextPrint() throws UnsupportedEncodingException {

        if (rtPrinter == null) {
            return;
        }

        TextSetting textSetting = new TextSetting();
        textSetting.setBold(SettingEnum.Enable);//加粗
        textSetting.setAlign(CommonEnum.ALIGN_MIDDLE);
//        textSetting.setFontStyle(SettingEnum.FONT_STYLE_SHADOW);
//        textSetting.setItalic(SettingEnum.Enable);
        textSetting.setDoubleHeight(SettingEnum.Enable);//倍高
        textSetting.setDoubleWidth(SettingEnum.Enable);//倍宽
        textSetting.setDoublePrinting(SettingEnum.Enable);//重叠打印
//        textSetting.setPinPrintMode(CommonEnum.PIN_PRINT_MODE_Bidirectional);
        textSetting.setUnderline(SettingEnum.Enable);//下划线

        CmdFactory cmdFactory = new PinFactory();
        Cmd cmd = cmdFactory.create();
        cmd.append(cmd.getHeaderCmd());//初始化
        cmd.append(cmd.getTextCmd(textSetting, printStr, "GBK"));
        cmd.append(cmd.getLFCRCmd());//换行
        cmd.append(cmd.getEndCmd());//退纸

        rtPrinter.writeMsgAsync(cmd.getAppendCmds());
    }

    private void zplTextPrint() {

        CmdFactory zplFac = new ZplFactory();
        Cmd zplCmd = zplFac.create();

        zplCmd.append(zplCmd.getHeaderCmd());
        CommonSetting commonSetting = new CommonSetting();
        commonSetting.setLableSizeBean(new LableSizeBean(80, 40));
        commonSetting.setPrintDirection(PrintDirection.REVERSE);
        zplCmd.append(zplCmd.getHeaderCmd());
        zplCmd.append(zplCmd.getCommonSettingCmd(commonSetting));

        TextSetting textSetting = new TextSetting();
        textSetting.setZplFontTypeEnum(ZplFontTypeEnum.FONT_2);
        textSetting.setTxtPrintPosition(new Position(80, 80));
        textSetting.setPrintRotation(PrintRotation.Rotate0);
        textSetting.setxMultiplication(2);
        textSetting.setyMultiplication(2);
        try {
            zplCmd.append(zplCmd.getTextCmd(textSetting, printStr));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        try {
            zplCmd.append(zplCmd.getPrintCopies(1));
        } catch (SdkException e) {
            e.printStackTrace();
        }
        zplCmd.append(zplCmd.getEndCmd());
        if (rtPrinter != null) {
            rtPrinter.writeMsgAsync(zplCmd.getAppendCmds());
        }

    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_txtprint:
                try {
                    textPrint();
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                break;
            default:
                break;
        }
    }

}
