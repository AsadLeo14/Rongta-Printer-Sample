package com.printer.example.activity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

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
import com.rt.printerlibrary.enumerate.BarcodeStringPosition;
import com.rt.printerlibrary.enumerate.BarcodeType;
import com.rt.printerlibrary.enumerate.CommonEnum;
import com.rt.printerlibrary.enumerate.CpclFontTypeEnum;
import com.rt.printerlibrary.enumerate.ESCFontTypeEnum;
import com.rt.printerlibrary.enumerate.EscBarcodePrintOritention;
import com.rt.printerlibrary.enumerate.PrintDirection;
import com.rt.printerlibrary.enumerate.PrintRotation;
import com.rt.printerlibrary.enumerate.QrcodeEccLevel;
import com.rt.printerlibrary.enumerate.SettingEnum;
import com.rt.printerlibrary.enumerate.TscFontTypeEnum;
import com.rt.printerlibrary.enumerate.ZplFontTypeEnum;
import com.rt.printerlibrary.exception.SdkException;
import com.rt.printerlibrary.factory.cmd.CmdFactory;
import com.rt.printerlibrary.printer.RTPrinter;
import com.rt.printerlibrary.setting.BarcodeSetting;
import com.rt.printerlibrary.setting.BitmapSetting;
import com.rt.printerlibrary.setting.CommonSetting;
import com.rt.printerlibrary.setting.TextSetting;

import java.io.UnsupportedEncodingException;

public class TempletPrintActivity extends BaseActivity {

    private Button btn_template_ESC_80;

    private RTPrinter rtPrinter;
    private Bitmap bmp;
    private String title, content_tel, content_email, barcode_str;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_templet_print);
        initView();
        addListener();
        init();
    }

    @Override
    public void initView() {
        title = getString(R.string.temp1_title1_printer_tech);
        content_tel = getString(R.string.temp1_content1_tel);
        content_email = getString(R.string.temp1_content2_email);
        barcode_str = "123456789";

        btn_template_ESC_80 = findViewById(R.id.btn_template_ESC_80);

        if (BaseApplication.getInstance().getCurrentCmdType() != BaseEnum.CMD_ESC) {
            btn_template_ESC_80.setVisibility(View.GONE);
        }
    }

    @Override
    public void addListener() {

    }

    @Override
    public void init() {
        rtPrinter = BaseApplication.getInstance().getRtPrinter();
    }

    public void onBtnClick(View v) {
        switch (v.getId()) {
            case R.id.btn_template1:
                try {
                    printTemplet();
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                } catch (SdkException e) {
                    e.printStackTrace();
                }
                break;
            case R.id.btn_template2:
//                printTempletIndiaTest();
                escTicketTemplet();
                break;
            case R.id.btn_template_ESC_80:
                esc80TempPrint();
                break;
            default:
                break;
        }
    }

    private void esc80TempPrint() {
        CmdFactory escFac = new EscFactory();
        Cmd escCmd = escFac.create();
        escCmd.append(escCmd.getHeaderCmd());//初始化
        escCmd.setChartsetName("UTF-8");

        CommonSetting commonSetting = new CommonSetting();
        commonSetting.setAlign(CommonEnum.ALIGN_LEFT);
        escCmd.append(escCmd.getCommonSettingCmd(commonSetting));

        TextSetting textSetting = new TextSetting();
        textSetting.setEscFontType(ESCFontTypeEnum.FONT_A_12x24);

        try {
            String preBlank = "        ";
            escCmd.append(escCmd.getTextCmd(textSetting, preBlank + "Table No:6(0)     Date:09/04/18"));
            escCmd.append(escCmd.getLFCRCmd());
            escCmd.append(escCmd.getTextCmd(textSetting, preBlank + "Kot No:5          Time:18:10"));
            escCmd.append(escCmd.getLFCRCmd());
            escCmd.append(escCmd.getTextCmd(textSetting, preBlank + "--------------------------------"));
            escCmd.append(escCmd.getLFCRCmd());

            textSetting.setEscFontType(ESCFontTypeEnum.FONT_B_9x24);
            escCmd.append(escCmd.getTextCmd(textSetting, preBlank + preBlank + "Item" + preBlank + preBlank + "  Qty"));
            escCmd.append(escCmd.getLFCRCmd());

            textSetting.setEscFontType(ESCFontTypeEnum.FONT_A_12x24);
            escCmd.append(escCmd.getTextCmd(textSetting, preBlank + "--------------------------------"));
            escCmd.append(escCmd.getLFCRCmd());

            textSetting.setBold(SettingEnum.Enable);
            escCmd.append(escCmd.getTextCmd(textSetting, preBlank + "Cheese Pasta        1 No"));
            escCmd.append(escCmd.getLFCRCmd());
            escCmd.append(escCmd.getTextCmd(textSetting, preBlank + "Red Sauce Pasta     1 No"));
            escCmd.append(escCmd.getLFCRCmd());
            escCmd.append(escCmd.getTextCmd(textSetting, preBlank + "JAIN BURGER         1 No"));
            escCmd.append(escCmd.getLFCRCmd());

            textSetting.setBold(SettingEnum.Disable);
            textSetting.setEscFontType(ESCFontTypeEnum.FONT_B_9x24);
            escCmd.append(escCmd.getTextCmd(textSetting, preBlank + "    no cheese"));
            escCmd.append(escCmd.getLFCRCmd());

            textSetting.setEscFontType(ESCFontTypeEnum.FONT_A_12x24);
            textSetting.setBold(SettingEnum.Enable);
            escCmd.append(escCmd.getTextCmd(textSetting, preBlank + "Masala Cheesy Gri   1 No"));
            escCmd.append(escCmd.getLFCRCmd());

            textSetting.setBold(SettingEnum.Disable);
            textSetting.setEscFontType(ESCFontTypeEnum.FONT_B_9x24);
            escCmd.append(escCmd.getTextCmd(textSetting, preBlank + "    Krispe"));
            escCmd.append(escCmd.getLFCRCmd());
            escCmd.append(escCmd.getTextCmd(textSetting, preBlank + "    Spicy"));
            escCmd.append(escCmd.getLFCRCmd());

            textSetting.setEscFontType(ESCFontTypeEnum.FONT_A_12x24);
            escCmd.append(escCmd.getTextCmd(textSetting, preBlank + "--------------------------------"));
            escCmd.append(escCmd.getLFCRCmd());
            textSetting.setBold(SettingEnum.Enable);
            escCmd.append(escCmd.getTextCmd(textSetting, preBlank + "         Total       4"));
            escCmd.append(escCmd.getLFCRCmd());
            escCmd.append(escCmd.getLFCRCmd());
            escCmd.append(escCmd.getLFCRCmd());
            escCmd.append(escCmd.getLFCRCmd());
            escCmd.append(escCmd.getLFCRCmd());
            rtPrinter.writeMsgAsync(escCmd.getAppendCmds());
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

    }

    private void printTempletIndiaTest() {
        switch (BaseApplication.getInstance().getCurrentCmdType()) {
            case BaseEnum.CMD_ESC:
                escPrintIndiaTest();
                break;
            case BaseEnum.CMD_TSC:
                break;
            case BaseEnum.CMD_CPCL:
                break;
            case BaseEnum.CMD_ZPL:
                break;
            case BaseEnum.CMD_PIN:
                break;
            default:
                break;
        }
    }

    private void escPrintIndiaTest() {
        if (rtPrinter == null) {
            return;
        }
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    escTempletIndiaTest();
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                } catch (SdkException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    private void printTemplet() throws UnsupportedEncodingException, SdkException {
        switch (BaseApplication.getInstance().getCurrentCmdType()) {
            case BaseEnum.CMD_ESC:
                escPrint();
                break;
            case BaseEnum.CMD_TSC:
                tscPrint();
                break;
            case BaseEnum.CMD_CPCL:
                cpclPrint();
                break;
            case BaseEnum.CMD_ZPL:
                zplPrint();
                break;
            case BaseEnum.CMD_PIN:
                pinPrint();
                break;
            default:
                break;
        }
    }

    private void pinPrint() throws UnsupportedEncodingException, SdkException {
        new Thread(new Runnable() {
            @Override
            public void run() {

                showProgressDialog(null);

                if (rtPrinter == null) {
                    return;
                }
                CmdFactory fac = new PinFactory();
                Cmd cmd = fac.create();

                TextSetting textSetting = new TextSetting();
                textSetting.setBold(SettingEnum.Enable);//加粗
                textSetting.setDoubleHeight(SettingEnum.Enable);//倍高
                textSetting.setDoubleWidth(SettingEnum.Enable);//倍宽
                textSetting.setDoublePrinting(SettingEnum.Disable);//重叠打印
//        textSetting.setPinPrintMode(CommonEnum.PIN_PRINT_MODE_Bidirectional);
                textSetting.setUnderline(SettingEnum.Disable);//下划线
                textSetting.setAlign(CommonEnum.ALIGN_MIDDLE);

                cmd.append(cmd.getHeaderCmd());//初始化
                try {
                    cmd.append(cmd.getTextCmd(textSetting, title, "GBK"));
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                cmd.append(cmd.getLFCRCmd());//换行

                textSetting.setBold(SettingEnum.Disable);//取消加粗
                textSetting.setDoubleHeight(SettingEnum.Disable);//取消倍高
                textSetting.setDoubleWidth(SettingEnum.Disable);//取消倍宽
                try {
                    cmd.append(cmd.getTextCmd(textSetting, content_tel, "GBK"));
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                cmd.append(cmd.getLFCRCmd());//换行
                try {
                    cmd.append(cmd.getTextCmd(textSetting, content_email, "GBK"));
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                cmd.append(cmd.getLFCRCmd());//换行

                cmd.append(cmd.getEndCmd());//退纸

                rtPrinter.writeMsg(cmd.getAppendCmds());
            }
        }).start();

    }

    private void zplPrint() throws UnsupportedEncodingException, SdkException {
        if (rtPrinter == null) {
            return;
        }
        new Thread(new Runnable() {
            @Override
            public void run() {
                showProgressDialog(null);
                CmdFactory fac = new ZplFactory();
                Cmd cmd = fac.create();

                cmd.append(cmd.getHeaderCmd());

                CommonSetting commonSetting = new CommonSetting();
                commonSetting.setLabelGap(2);
                commonSetting.setPrintDirection(PrintDirection.REVERSE);//打印方向
                commonSetting.setLableSizeBean(new LableSizeBean(80, 80));//label width = 80mm, label height = 80mm
                cmd.append(cmd.getCommonSettingCmd(commonSetting));


                TextSetting textSetting = new TextSetting();
                textSetting.setZplFontTypeEnum(ZplFontTypeEnum.FONT_DOWNLOAD_FONT);
                textSetting.setTxtPrintPosition(new Position(130, 80));
                textSetting.setPrintRotation(PrintRotation.Rotate0);
                textSetting.setZplHeightFactor(64);// >10
                textSetting.setZplWidthFactor(64);//>10
                try {
                    cmd.append(cmd.getTextCmd(textSetting, title));
                    textSetting.setTxtPrintPosition(new Position(160, 140));
                    textSetting.setZplHeightFactor(2);// 1~10
                    textSetting.setZplWidthFactor(2);// 1~10
                    textSetting.setZplFontTypeEnum(ZplFontTypeEnum.FONT_2);
                    cmd.append(cmd.getTextCmd(textSetting, content_tel));
                    textSetting.setTxtPrintPosition(new Position(70, 180));
                    cmd.append(cmd.getTextCmd(textSetting, content_email));
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                BitmapSetting bitmapSetting = new BitmapSetting();
                bitmapSetting.setBimtapLimitWidth(40);//限制图片最大宽度 58打印机=48mm， 80打印机=72mm
                bitmapSetting.setPrintPostion(new Position(250, 220));
                if (bmp == null) {
                    bmp = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher);
                }
                try {
                    cmd.append(cmd.getBitmapCmd(bitmapSetting, Bitmap.createBitmap(bmp)));
                } catch (SdkException e) {
                    e.printStackTrace();
                }
                cmd.append(cmd.getLFCRCmd());

                BarcodeSetting barcodeSetting = new BarcodeSetting();
                barcodeSetting.setBarcodeStringPosition(BarcodeStringPosition.BELOW_BARCODE);
                barcodeSetting.setPosition(new Position(120, 280));
                barcodeSetting.setPrintRotation(PrintRotation.Rotate0);
                barcodeSetting.setHeightInDot(72);//accept value:1~255
                barcodeSetting.setBarcodeWidth(3);//accept value:2~6
                try {
                    cmd.append(cmd.getBarcodeCmd(BarcodeType.CODE128, barcodeSetting, barcode_str));
                } catch (SdkException e) {
                    e.printStackTrace();
                }
                cmd.append(cmd.getLFCRCmd());

                barcodeSetting.setPosition(new Position(200, 420));
                barcodeSetting.setQrcodeDotSize(5);//accept value: Esc(1~15), Tsc(1~10)
                barcodeSetting.setQrcodeEccLevel(QrcodeEccLevel.L);
                try {
                    cmd.append(cmd.getBarcodeCmd(BarcodeType.QR_CODE, barcodeSetting, content_email));
                } catch (SdkException e) {
                    e.printStackTrace();
                }

                cmd.append(cmd.getLFCRCmd());
                cmd.append(cmd.getLFCRCmd());
                cmd.append(cmd.getLFCRCmd());

                try {
                    cmd.append(cmd.getPrintCopies(1));//ZPL must add this function, print copies settings， ZPL必须要加上这个方法，打印份数
                } catch (SdkException e) {
                    e.printStackTrace();
                }
                cmd.append(cmd.getEndCmd());
                String str = new String(cmd.getAppendCmds());
                Log.e("sss", str);
                rtPrinter.writeMsg(cmd.getAppendCmds());
                hideProgressDialog();
            }
        }).start();

    }

    private void cpclPrint() throws UnsupportedEncodingException, SdkException {
        if (rtPrinter == null) {
            return;
        }

        CmdFactory cpclFac = new CpclFactory();
        Cmd cmd = cpclFac.create();

        cmd.append(cmd.getCpclHeaderCmd(80, 80, 1));//初始化，标签宽度80mm, 长度80mm， 打印份数为1

        TextSetting textSetting = new TextSetting();
        textSetting.setCpclFontTypeEnum(CpclFontTypeEnum.Font_3);
        textSetting.setTxtPrintPosition(new Position(80, 80));
        textSetting.setPrintRotation(PrintRotation.Rotate0);
        textSetting.setxMultiplication(2);
        textSetting.setyMultiplication(2);
        try {
            cmd.append(cmd.getTextCmd(textSetting, title));
            textSetting.setTxtPrintPosition(new Position(120, 140));
            textSetting.setxMultiplication(1);
            textSetting.setyMultiplication(1);
            textSetting.setCpclFontTypeEnum(CpclFontTypeEnum.Font_2);
            cmd.append(cmd.getTextCmd(textSetting, content_tel));
            textSetting.setTxtPrintPosition(new Position(80, 180));
            cmd.append(cmd.getTextCmd(textSetting, content_email));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        BitmapSetting bitmapSetting = new BitmapSetting();
        bitmapSetting.setBimtapLimitWidth(40);//限制图片最大宽度 58打印机=48mm， 80打印机=72mm
        bitmapSetting.setPrintPostion(new Position(250, 220));
        if (bmp == null) {
            bmp = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher);
        }
        cmd.append(cmd.getBitmapCmd(bitmapSetting, Bitmap.createBitmap(bmp)));
        cmd.append(cmd.getLFCRCmd());

        BarcodeSetting barcodeSetting = new BarcodeSetting();
        barcodeSetting.setEscBarcodePrintOritention(EscBarcodePrintOritention.Rotate0);
        barcodeSetting.setBarcodeStringPosition(BarcodeStringPosition.BELOW_BARCODE);
        barcodeSetting.setPosition(new Position(180, 280));
        barcodeSetting.setPrintRotation(PrintRotation.Rotate0);
        barcodeSetting.setHeightInDot(72);//accept value:1~255
        barcodeSetting.setBarcodeWidth(3);//accept value:2~6
        cmd.append(cmd.getBarcodeCmd(BarcodeType.CODE128, barcodeSetting, barcode_str));
        cmd.append(cmd.getLFCRCmd());

        barcodeSetting.setPosition(new Position(220, 420));
        barcodeSetting.setQrcodeDotSize(5);//accept value: Esc(1~15), Tsc(1~10)
        barcodeSetting.setQrcodeEccLevel(QrcodeEccLevel.L);
        cmd.append(cmd.getBarcodeCmd(BarcodeType.QR_CODE, barcodeSetting, content_email));

        cmd.append(cmd.getLFCRCmd());
        cmd.append(cmd.getLFCRCmd());
        cmd.append(cmd.getLFCRCmd());


        cmd.append(cmd.getEndCmd());
        rtPrinter.writeMsg(cmd.getAppendCmds());
    }

    private void tscPrint() throws UnsupportedEncodingException, SdkException {
        if (rtPrinter == null) {
            return;
        }

        CmdFactory tscFac = new TscFactory();
        Cmd cmd = tscFac.create();

        cmd.append(cmd.getHeaderCmd());

        CommonSetting commonSetting = new CommonSetting();
        commonSetting.setLabelGap(2);
        commonSetting.setPrintDirection(PrintDirection.NORMAL);
        commonSetting.setLableSizeBean(new LableSizeBean(80, 80));//label width = 80mm, label height = 80mm
        cmd.append(cmd.getCommonSettingCmd(commonSetting));


        TextSetting textSetting = new TextSetting();
        textSetting.setTscFontTypeEnum(TscFontTypeEnum.Font_16x24_For_English_Number);
        textSetting.setTxtPrintPosition(new Position(80, 80));
        textSetting.setPrintRotation(PrintRotation.Rotate0);
        textSetting.setxMultiplication(2);
        textSetting.setyMultiplication(2);
        try {
            cmd.append(cmd.getTextCmd(textSetting, title));
            textSetting.setTxtPrintPosition(new Position(120, 140));
            textSetting.setxMultiplication(1);
            textSetting.setyMultiplication(1);
            textSetting.setTscFontTypeEnum(TscFontTypeEnum.Font_12x20_For_English_Number);
            cmd.append(cmd.getTextCmd(textSetting, content_tel));
            textSetting.setTxtPrintPosition(new Position(80, 180));
            cmd.append(cmd.getTextCmd(textSetting, content_email));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        BitmapSetting bitmapSetting = new BitmapSetting();
        bitmapSetting.setBimtapLimitWidth(40);//限制图片最大宽度 58打印机=48mm， 80打印机=72mm
        bitmapSetting.setPrintPostion(new Position(250, 220));
        if (bmp == null) {
            bmp = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher);
        }
        cmd.append(cmd.getBitmapCmd(bitmapSetting, Bitmap.createBitmap(bmp)));
        cmd.append(cmd.getLFCRCmd());

        BarcodeSetting barcodeSetting = new BarcodeSetting();
        barcodeSetting.setBarcodeStringPosition(BarcodeStringPosition.BELOW_BARCODE);
        barcodeSetting.setPosition(new Position(180, 280));
        barcodeSetting.setPrintRotation(PrintRotation.Rotate0);
        barcodeSetting.setHeightInDot(72);//accept value:1~255
        barcodeSetting.setBarcodeWidth(3);//accept value:2~6
        cmd.append(cmd.getBarcodeCmd(BarcodeType.CODE128, barcodeSetting, barcode_str));
        cmd.append(cmd.getLFCRCmd());

        barcodeSetting.setPosition(new Position(220, 420));
        barcodeSetting.setQrcodeDotSize(5);//accept value: Esc(1~15), Tsc(1~10)
        barcodeSetting.setQrcodeEccLevel(QrcodeEccLevel.L);
        cmd.append(cmd.getBarcodeCmd(BarcodeType.QR_CODE, barcodeSetting, content_email));

        cmd.append(cmd.getLFCRCmd());
        cmd.append(cmd.getLFCRCmd());
        cmd.append(cmd.getLFCRCmd());

        cmd.append(cmd.getPrintCopies(1));//TSC must add this function， TSC必须要加上这个方法，打印份数
        cmd.append(cmd.getEndCmd());
        rtPrinter.writeMsgAsync(cmd.getAppendCmds());
    }

    private void escPrint() throws UnsupportedEncodingException, SdkException {
        if (rtPrinter == null) {
            return;
        }
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    escTemplet();
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                } catch (SdkException e) {
                    e.printStackTrace();
                }
            }
        }).start();

    }

    private void escTemplet() throws UnsupportedEncodingException, SdkException {
        CmdFactory escFac = new EscFactory();
        Cmd escCmd = escFac.create();
        escCmd.setChartsetName("UTF-8");
        TextSetting textSetting = new TextSetting();
        textSetting.setAlign(CommonEnum.ALIGN_MIDDLE);//对齐方式-左对齐，居中，右对齐
        textSetting.setBold(SettingEnum.Enable);//加粗
        textSetting.setUnderline(SettingEnum.Disable);//下划线
        textSetting.setIsAntiWhite(SettingEnum.Disable);//反白
        textSetting.setDoubleHeight(SettingEnum.Enable);//倍高
        textSetting.setDoubleWidth(SettingEnum.Enable);//倍宽
        textSetting.setItalic(SettingEnum.Disable);//斜体
        textSetting.setIsEscSmallCharactor(SettingEnum.Disable);//小字体
        escCmd.append(escCmd.getHeaderCmd());//初始化
        escCmd.append(escCmd.getTextCmd(textSetting, title));
        escCmd.append(escCmd.getLFCRCmd());//回车换行

        textSetting.setIsEscSmallCharactor(SettingEnum.Enable);
        textSetting.setBold(SettingEnum.Disable);
        textSetting.setDoubleHeight(SettingEnum.Disable);
        textSetting.setDoubleWidth(SettingEnum.Disable);
        escCmd.append(escCmd.getTextCmd(textSetting, content_tel));

        escCmd.append(escCmd.getLFCRCmd());
        textSetting.setUnderline(SettingEnum.Enable);
        escCmd.append(escCmd.getTextCmd(textSetting, content_email));

        escCmd.append(escCmd.getLFCRCmd());
        escCmd.append(escCmd.getLFCRCmd());

        BitmapSetting bitmapSetting = new BitmapSetting();
        bitmapSetting.setBimtapLimitWidth(40);//限制图片最大宽度 58打印机=48mm， 80打印机=72mm
        if (bmp == null) {
            bmp = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher);
        }
        escCmd.append(escCmd.getBitmapCmd(bitmapSetting, Bitmap.createBitmap(bmp)));
        escCmd.append(escCmd.getLFCRCmd());

        BarcodeSetting barcodeSetting = new BarcodeSetting();
        barcodeSetting.setEscBarcodePrintOritention(EscBarcodePrintOritention.Rotate0);
        barcodeSetting.setBarcodeStringPosition(BarcodeStringPosition.BELOW_BARCODE);
        barcodeSetting.setHeightInDot(72);//accept value:1~255
        barcodeSetting.setBarcodeWidth(3);//accept value:2~6
        escCmd.append(escCmd.getBarcodeCmd(BarcodeType.CODE128, barcodeSetting, barcode_str));
        escCmd.append(escCmd.getLFCRCmd());

        barcodeSetting.setQrcodeDotSize(5);//accept value: Esc(1~15), Tsc(1~10)
        barcodeSetting.setQrcodeEccLevel(QrcodeEccLevel.L);
        escCmd.append(escCmd.getBarcodeCmd(BarcodeType.QR_CODE, barcodeSetting, content_email));

        escCmd.append(escCmd.getLFCRCmd());
        escCmd.append(escCmd.getLFCRCmd());
        escCmd.append(escCmd.getLFCRCmd());

        rtPrinter.writeMsg(escCmd.getAppendCmds());
    }


    private void escTempletIndiaTest() throws UnsupportedEncodingException, SdkException {
        CmdFactory escFac = new EscFactory();
        Cmd escCmd = escFac.create();
        escCmd.append(escCmd.getHeaderCmd());//初始化
        escCmd.setChartsetName("UTF-8");

        CommonSetting commonSetting = new CommonSetting();
        commonSetting.setAlign(CommonEnum.ALIGN_MIDDLE);
        escCmd.append(escCmd.getCommonSettingCmd(commonSetting));

        BitmapSetting bitmapSetting = new BitmapSetting();
        bitmapSetting.setBimtapLimitWidth(48 * 8);//限制图片最大宽度 58打印机=48mm， 80打印机=72mm
        if (bmp == null) {
            bmp = BitmapFactory.decodeResource(getResources(), R.drawable.bill_bmptest);
        }
        escCmd.append(escCmd.getBitmapCmd(bitmapSetting, Bitmap.createBitmap(bmp)));
        escCmd.append(escCmd.getLFCRCmd());


        TextSetting textSetting = new TextSetting();
        textSetting.setAlign(CommonEnum.ALIGN_MIDDLE);//对齐方式-左对齐，居中，右对齐
        textSetting.setBold(SettingEnum.Enable);//加粗
        textSetting.setUnderline(SettingEnum.Disable);//下划线
        textSetting.setIsAntiWhite(SettingEnum.Disable);//反白
        textSetting.setDoubleHeight(SettingEnum.Enable);//倍高
        textSetting.setDoubleWidth(SettingEnum.Enable);//倍宽
        textSetting.setItalic(SettingEnum.Disable);//斜体
        textSetting.setIsEscSmallCharactor(SettingEnum.Disable);//小字体


        escCmd.append(escCmd.getTextCmd(textSetting, "India Test 1"));
        escCmd.append(escCmd.getLFCRCmd());//回车换行

        textSetting.setIsEscSmallCharactor(SettingEnum.Enable);
        textSetting.setBold(SettingEnum.Disable);
        textSetting.setDoubleHeight(SettingEnum.Disable);
        textSetting.setDoubleWidth(SettingEnum.Disable);
        escCmd.append(escCmd.getTextCmd(textSetting, "India Test 2"));

        escCmd.append(escCmd.getLFCRCmd());
        textSetting.setUnderline(SettingEnum.Enable);
        escCmd.append(escCmd.getTextCmd(textSetting, "India Test 3"));

        escCmd.append(escCmd.getLFCRCmd());
        escCmd.append(escCmd.getLFCRCmd());

//        BarcodeSetting barcodeSetting = new BarcodeSetting();
//        barcodeSetting.setEscBarcodePrintOritention(EscBarcodePrintOritention.Rotate0);
//        barcodeSetting.setBarcodeStringPosition(BarcodeStringPosition.BELOW_BARCODE);
//        barcodeSetting.setHeightInDot(72);//accept value:1~255
//        barcodeSetting.setBarcodeWidth(3);//accept value:2~6
//        escCmd.append(escCmd.getBarcodeCmd(BarcodeType.CODE128, barcodeSetting, barcode_str));
//        escCmd.append(escCmd.getLFCRCmd());
//
//        barcodeSetting.setQrcodeDotSize(5);//accept value: Esc(1~15), Tsc(1~10)
//        barcodeSetting.setQrcodeEccLevel(QrcodeEccLevel.L);
//        escCmd.append(escCmd.getBarcodeCmd(BarcodeType.QR_CODE, barcodeSetting, content_email));

        escCmd.append(escCmd.getLFCRCmd());
        escCmd.append(escCmd.getLFCRCmd());
        escCmd.append(escCmd.getLFCRCmd());

        rtPrinter.writeMsg(escCmd.getAppendCmds());
    }

    private void escTicketTemplet() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    CmdFactory escFac = new EscFactory();
                    Cmd escCmd = escFac.create();
                    escCmd.append(escCmd.getHeaderCmd());//初始化
                    escCmd.setChartsetName("UTF-8");

                    CommonSetting commonSetting = new CommonSetting();
                    commonSetting.setAlign(CommonEnum.ALIGN_MIDDLE);
                    escCmd.append(escCmd.getCommonSettingCmd(commonSetting));

                    BitmapSetting bitmapSetting = new BitmapSetting();
                    bitmapSetting.setBimtapLimitWidth(28 * 8);//限制图片最大宽度 58打印机=48mm， 80打印机=72mm
                    if (bmp == null) {
                        bmp = BitmapFactory.decodeResource(getResources(), R.drawable.logo_rongta);
                    }
                    try {
                        escCmd.append(escCmd.getBitmapCmd(bitmapSetting, Bitmap.createBitmap(bmp)));
                    } catch (SdkException e) {
                        e.printStackTrace();
                    }
                    escCmd.append(escCmd.getLFCRCmd());


                    TextSetting textSetting = new TextSetting();
                    textSetting.setAlign(CommonEnum.ALIGN_MIDDLE);//对齐方式-左对齐，居中，右对齐
                    textSetting.setBold(SettingEnum.Enable);//加粗
                    textSetting.setUnderline(SettingEnum.Disable);//下划线
                    textSetting.setIsAntiWhite(SettingEnum.Disable);//反白
                    textSetting.setDoubleHeight(SettingEnum.Enable);//倍高
                    textSetting.setDoubleWidth(SettingEnum.Enable);//倍宽
                    textSetting.setItalic(SettingEnum.Disable);//斜体
                    textSetting.setIsEscSmallCharactor(SettingEnum.Disable);//小字体

                    escCmd.append(escCmd.getTextCmd(textSetting, "The Red Rose"));
                    escCmd.append(escCmd.getLFCRCmd());//回车换行
                    textSetting.setBold(SettingEnum.Disable);
                    textSetting.setDoubleHeight(SettingEnum.Disable);//倍高
                    textSetting.setDoubleWidth(SettingEnum.Disable);//倍宽
                    textSetting.setIsEscSmallCharactor(SettingEnum.Enable);//小字体
                    escCmd.append(escCmd.getTextCmd(textSetting, "Indian Resturant"));
                    escCmd.append(escCmd.getLFCRCmd());
                    escCmd.append(escCmd.getTextCmd(textSetting, "Noida, Noida"));
                    escCmd.append(escCmd.getLFCRCmd());
                    escCmd.append(escCmd.getTextCmd(textSetting, "Website:http://www.xxx.com"));
                    escCmd.append(escCmd.getLFCRCmd());
                    escCmd.append(escCmd.getTextCmd(textSetting, "GSTIN No.:ROS2345ST"));
                    escCmd.append(escCmd.getLFCRCmd());
                    String line = "—————————————————————————————————————————";
                    textSetting.setBold(SettingEnum.Enable);
                    escCmd.append(escCmd.getTextCmd(textSetting, line));
                    escCmd.append(escCmd.getLFCRCmd());
                    textSetting.setBold(SettingEnum.Disable);
                    String bank = "                          ";
                    escCmd.append(escCmd.getTextCmd(textSetting, "Type:" + bank + "Table:2"));
                    escCmd.append(escCmd.getLFCRCmd());//回车换行
                    escCmd.append(escCmd.getTextCmd(textSetting, "Type:" + bank + "Table:2"));
                    escCmd.append(escCmd.getLFCRCmd());//回车换行
                    escCmd.append(escCmd.getTextCmd(textSetting, "Type:" + bank + "Table:2"));
                    escCmd.append(escCmd.getLFCRCmd());//回车换行
                    textSetting.setBold(SettingEnum.Enable);
                    escCmd.append(escCmd.getTextCmd(textSetting, line));
                    escCmd.append(escCmd.getLFCRCmd());

                    escCmd.append(escCmd.getTextCmd(textSetting, "Item Name      Qty       Rate      Amount\n"));

                    textSetting.setBold(SettingEnum.Enable);
                    escCmd.append(escCmd.getTextCmd(textSetting, line));
                    escCmd.append(escCmd.getLFCRCmd());

                    textSetting.setBold(SettingEnum.Disable);
                    escCmd.append(escCmd.getTextCmd(textSetting, "Item111        2.00      83.33    150.0000\n"));
                    escCmd.append(escCmd.getTextCmd(textSetting, "Item111        2.00      83.33    150.0000\n"));
                    escCmd.append(escCmd.getTextCmd(textSetting, "Item111        2.00      83.33    150.0000\n"));
                    escCmd.append(escCmd.getTextCmd(textSetting, "Item111        2.00      83.33    150.0000\n"));
                    escCmd.append(escCmd.getTextCmd(textSetting, "Item111        2.00      83.33    150.0000\n"));

                    textSetting.setBold(SettingEnum.Enable);
                    escCmd.append(escCmd.getTextCmd(textSetting, line));
                    escCmd.append(escCmd.getLFCRCmd());

                    textSetting.setBold(SettingEnum.Disable);
                    escCmd.append(escCmd.getTextCmd(textSetting, "            Sub Total             750.0000\n"));
                    escCmd.append(escCmd.getTextCmd(textSetting, "            @Oval                        0\n"));

                    textSetting.setBold(SettingEnum.Enable);
                    escCmd.append(escCmd.getTextCmd(textSetting, line));
                    escCmd.append(escCmd.getLFCRCmd());

                    textSetting.setIsEscSmallCharactor(SettingEnum.Disable);//小字体
                    textSetting.setBold(SettingEnum.Enable);//加粗
                    escCmd.append(escCmd.getTextCmd(textSetting, "     Net Amount         2524.98\n"));

                    textSetting.setBold(SettingEnum.Enable);
                    textSetting.setIsEscSmallCharactor(SettingEnum.Enable);//小字体
                    escCmd.append(escCmd.getTextCmd(textSetting, line));
                    escCmd.append(escCmd.getLFCRCmd());

                    textSetting.setBold(SettingEnum.Disable);
                    textSetting.setIsEscSmallCharactor(SettingEnum.Enable);//小字体
                    escCmd.append(escCmd.getTextCmd(textSetting, "KOT(s): KOT_23,KOT_24,KOT_31               \n"));
                    escCmd.append(escCmd.getTextCmd(textSetting, "Guest Signature:              ___________\n"));
                    escCmd.append(escCmd.getTextCmd(textSetting, "Authorised Signatory:         ___________\n"));
                    escCmd.append(escCmd.getTextCmd(textSetting, "Cashier:                                   \n"));

                    textSetting.setItalic(SettingEnum.Enable);
                    textSetting.setAlign(CommonEnum.ALIGN_MIDDLE);
                    textSetting.setIsEscSmallCharactor(SettingEnum.Disable);//小字体
                    escCmd.append(escCmd.getTextCmd(textSetting, "Have a nice day.\nThank you visit again"));

                    escCmd.append(escCmd.getLFCRCmd());
                    escCmd.append(escCmd.getLFCRCmd());
                    escCmd.append(escCmd.getLFCRCmd());
                    escCmd.append(escCmd.getLFCRCmd());
                    escCmd.append(escCmd.getLFCRCmd());

                    rtPrinter.writeMsg(escCmd.getAppendCmds());

                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            }
        }).start();

    }

}
