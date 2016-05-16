package com.example.eagle.lalala.PictureWork;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Environment;
import android.util.Base64;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.Random;

/**
 * Created by NeilHY on 2016/4/19.
 */
public class HandlePicture  {

    public static String FILENAME="WeMarkPhoto";
    public static int reqWidth=480;
    public static int reqHeight=800;
//    public static String FILEPATH;

    /**
     * 计算图片的缩放值
     *
     * @param options
     * @param reqWidth
     * @param reqHeight
     * @return int
     */
    public static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight){
        final int height=options.outHeight;
        final int width=options.outWidth;
        int inSampleSize=1;
        if(height>width){
            if(height>reqHeight||width>reqWidth){
                final int heightRatio=Math.round((float)height/(float)reqHeight);
                final int widthRatio=Math.round((float)width/(float)reqWidth);
                inSampleSize=heightRatio<widthRatio?heightRatio:widthRatio;
            }
        }else{
            if(width>reqHeight||height>reqWidth){
                final int heightRatio=Math.round((float)width/(float)reqHeight);
                final int widthRatio=Math.round((float)height/(float)reqWidth);
                inSampleSize=heightRatio<widthRatio?heightRatio:widthRatio;
            }
        }
        return inSampleSize;
    }

    /**
     * 根据路径获得突破并压缩返回bitmap用于显示
     *
     * @param filePath
     * @param reqWidth
     * @param reqHeight
     * @return Bitmap
     */
    public static Bitmap decodeSampleBitmapFromPath(String filePath, int reqWidth, int reqHeight){
        final BitmapFactory.Options options=new BitmapFactory.Options();
        options.inJustDecodeBounds=true;
        BitmapFactory.decodeFile(filePath, options);
        //BitmapFactory.decodeResource(res, resId,options);
        options.inSampleSize=calculateInSampleSize(options,reqWidth,reqHeight);
        options.inJustDecodeBounds=false;
        return BitmapFactory.decodeFile(filePath, options);
    }

    /**
     * 把String转换成Bitmap
     *
     * @param filePath
     * @return
     */
    public static String bitmapToString(String filePath) {

        Bitmap bm = decodeSampleBitmapFromPath(filePath,reqWidth,reqHeight);

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.PNG, 40, baos);
        byte[] b = baos.toByteArray();

        return Base64.encodeToString(b, Base64.DEFAULT);
    }

    /**
     * 把Bitmap转换成String
     *
     * @param bitmap
     * @return
     */
    public static String bitmapToString(Bitmap bitmap) {

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 40, baos);
        byte[] b = baos.toByteArray();

        return Base64.encodeToString(b, Base64.DEFAULT);
    }

    /**
     * string转成bitmap
     *
     * @param string
     */
    public static Bitmap StringToBitmap(String string)
    {
        // OutputStream out;
        Bitmap bitmap = null;
        try
        {
            // out = new FileOutputStream("/sdcard/aa.jpg");
            byte[] bitmapArray;
            bitmapArray = Base64.decode(string, Base64.DEFAULT);
            bitmap =
                    BitmapFactory.decodeByteArray(bitmapArray, 0,
                            bitmapArray.length);
            // bitmap.compress(Bitmap.CompressFormat.PNG, 100, out);
            return bitmap;
        }
        catch (Exception e)
        {
            return null;
        }
    }


    /**
     * 根据路径删除图片
     *
     * @param path
     */
    public static void deleteTempFile(String path) {
        File file = new File(path);
        if (file.exists()) {
            file.delete();
        }
    }

    /**
     * 获取保存图片的目录
     * 及计算图片的路径
     * @return
     */
    public static File createFileForPhoto(){
        boolean SDcardExist= Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
        File outputImage = null;
        if(SDcardExist == true) { //如果有SD卡
            outputImage= new File(Environment.getExternalStorageDirectory(), FILENAME);
        }else{//如果没有SD卡，则在系统根目录下创建文件
            outputImage=new File(Environment.getRootDirectory(),FILENAME);
        }
        if (!outputImage.exists()) {//如果文件不存在
            outputImage.mkdir();//新建文件夹
        }
        Random random = new Random(System.currentTimeMillis());//随机生成照片id
        int imageId=Math.abs(random.nextInt())%10000;

        File filex=null;
        if(outputImage.isDirectory()){
            String path=outputImage.getAbsolutePath();
            filex=new File(path, imageId+".jpg");
            try {
                if (filex.exists()) {//如果文件存在
                    filex.delete();
                }
                filex.createNewFile();//新建文件
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return filex;
    }

    public static File createFileForBackground() {
        boolean SDcardExist= Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
        File outputImage = null;
        if(SDcardExist == true) { //如果有SD卡
            outputImage= new File(Environment.getExternalStorageDirectory(), FILENAME);
        }else{//如果没有SD卡，则在系统根目录下创建文件
            outputImage=new File(Environment.getRootDirectory(),FILENAME);
        }
        if (!outputImage.exists()) {//如果文件不存在
            outputImage.mkdir();//新建文件夹
        }
//        Random random = new Random(System.currentTimeMillis());//随机生成照片id
//        int imageId=Math.abs(random.nextInt())%10000;

        File filex=null;
        if(outputImage.isDirectory()){
            String path=outputImage.getAbsolutePath();
            filex=new File(path, "background.png");
            try {
                if (filex.exists()) {//如果文件存在
                    filex.delete();
                }
                filex.createNewFile();//新建文件
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return filex;
    }

    public static File createFileForIcon() {
        boolean SDcardExist= Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
        File outputImage = null;
        if(SDcardExist == true) { //如果有SD卡
            outputImage= new File(Environment.getExternalStorageDirectory(), FILENAME);
        }else{//如果没有SD卡，则在系统根目录下创建文件
            outputImage=new File(Environment.getRootDirectory(),FILENAME);
        }
        if (!outputImage.exists()) {//如果文件不存在
            outputImage.mkdir();//新建文件夹
        }
//        Random random = new Random(System.currentTimeMillis());//随机生成照片id
//        int imageId=Math.abs(random.nextInt())%10000;

        File filex=null;
        if(outputImage.isDirectory()){
            String path=outputImage.getAbsolutePath();
            filex=new File(path, "icon.png");
            try {
                if (filex.exists()) {//如果文件存在
                    filex.delete();
                }
                filex.createNewFile();//新建文件
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return filex;
    }

    /**
     * 计算bitmap的大小
     * @param bitmap
     * @return
     */
    public static int getBitmapSize(Bitmap bitmap){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT){    //API 19
            return bitmap.getAllocationByteCount();
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR1){//API 12
            return bitmap.getByteCount();
        }
        return bitmap.getRowBytes() * bitmap.getHeight();                //earlier version
    }

    /**
     * Bitmap.CompressFormat.JPEG 和 Bitmap.CompressFormat.PNG
     * JPEG 与 PNG 的是区别在于 JPEG是有损数据图像，PNG使用从LZ77派生的无损数据压缩算法。
     * 这里建议使用PNG格式保存
     * 100 表示的是质量为100%。当然，也可以改变成你所需要的百分比质量。
     * os 是定义的字节输出流
     *
     * .compress() 方法是将Bitmap压缩成指定格式和质量的输出流
     */
    public static byte[] bitmapTobyte(Bitmap bitmap) {
        ByteArrayOutputStream arrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, arrayOutputStream);
        return arrayOutputStream.toByteArray();
    }

    /**
     * 根据Bitmap字节数据转换成 Bitmap对象
     * BitmapFactory.decodeByteArray() 方法对字节数据，从0到字节的长进行解码，生成Bitmap对像。
     **/
    public static Bitmap byteToBitmap(byte[] bypes) {
        Bitmap bmpout = BitmapFactory.decodeByteArray(bypes, 0, bypes.length);
        return bmpout;
    }
}
