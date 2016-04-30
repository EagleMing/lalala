package com.example.eagle.lalala.Fragment;

import android.content.Context;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps2d.AMap;
import com.amap.api.maps2d.CameraUpdateFactory;
import com.amap.api.maps2d.LocationSource;
import com.amap.api.maps2d.MapView;
import com.amap.api.maps2d.model.BitmapDescriptorFactory;
import com.amap.api.maps2d.model.CameraPosition;
import com.amap.api.maps2d.model.LatLng;
import com.amap.api.maps2d.model.Marker;
import com.amap.api.maps2d.model.MarkerOptions;
import com.amap.api.maps2d.model.MyLocationStyle;
import com.example.eagle.lalala.Activity.MainActivity;
import com.example.eagle.lalala.NetWork.HttpCallbackListener;
import com.example.eagle.lalala.NetWork.HttpUtil;
import com.example.eagle.lalala.PDM.MarkItemResponse;
import com.example.eagle.lalala.R;
import com.google.android.gms.common.api.GoogleApiClient;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link map_frag.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the  factory method to
 * create an instance of this fragment.
 */
@SuppressWarnings("JavadocReference")
public class MapFragment extends Fragment implements LocationSource,
        AMapLocationListener, AMap.OnCameraChangeListener  {


    private MapView mapView;
    private AMap aMap;

    private OnLocationChangedListener mListener;
    private AMapLocationClient mlocationClient;
    private AMapLocationClientOption mLocationOption;
    private Button locate_btn;

    private int counter=91;//用来计算异步类多久执行一次
    private double lastLatitude;
    private double lastLongitude;

    private ArrayList<MarkItemResponse> markItems;
    private ArrayList<Marker> markers;
    private static final String serviceUrl="http://119.29.166.177:8080/getAroundMarks";
    private JSONArray MarksjsonArray;
//    private Marker marker;
//    private ArrayList<Marker> markersList=new ArrayList<Marker>();

    private Handler handler = new Handler(){
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    addMarkers();

                    break;
                case -1:

                    Toast.makeText(getActivity(),"获得周边Marks失败",Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    };


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,@Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.frag_map, container, false);

        return v;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mapView = (MapView)view.findViewById(R.id.gdmapview);
        mapView.onCreate(savedInstanceState);
        init();
    }


    private void init() {
        if (aMap == null) {
            aMap = mapView.getMap();
            setUpMap();
        }
    }

    /**
     * 设置一些amap的属性
     */
    private void setUpMap() {

        aMap.setLocationSource((LocationSource) this);// 设置定位监听
        aMap.getUiSettings().setMyLocationButtonEnabled(true);// 设置默认定位按钮是否显示
        aMap.setMyLocationEnabled(true);// 设置为true表示显示定位层并可触发定位，false表示隐藏定位层并不可触发定位，默认是false
        aMap.getUiSettings().setScaleControlsEnabled(true);//显示比例尺
        aMap.moveCamera(CameraUpdateFactory.zoomTo(aMap.getMaxZoomLevel()));//自动放大到当前最大比例尺

        // 自定义系统定位小蓝点
        MyLocationStyle myLocationStyle = new MyLocationStyle();
        myLocationStyle.myLocationIcon(BitmapDescriptorFactory
                .fromResource(R.drawable.location_marker));// 设置小蓝点的图标
        myLocationStyle.strokeColor(Color.argb(0, 0, 0, 0));// 设置圆形的边框颜色
        myLocationStyle.radiusFillColor(Color.argb(0, 0, 0, 0));// 设置圆形的填充颜色
        myLocationStyle.strokeWidth(0f);// 设置圆形的边框粗细
        aMap.setMyLocationStyle(myLocationStyle);

//        addMarkers(new LatLng(23.051629, 113.400453),"C12222","GL");
    }

//    public void addMarkers(LatLng latLng,String place, String usr) {
//
//        if(marker != null) marker.destroy();
//
//        marker = aMap.addMarker(
//                new MarkerOptions()
//                        .position(latLng)
//                        .anchor(0.5f,1f)
//                        .title(place)
//                        .snippet("Marked by: "+usr)
//                        .icon(
//                                BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)
//                        )
//                        .draggable(false)
//        );
//        markersList.add(markersList.size(),marker);
//        //"_location": "113.400453,23.051629",
//        //"_name": "华工大学城校区C12",
//        marker.showInfoWindow();
//
//    }

    public void addOneMarker(LatLng latLng,String placename, String usr) {
        Marker marker;
        marker = aMap.addMarker(
                new MarkerOptions()
                        .title(placename).snippet("Marked by: "+usr)
                        .position(latLng)
                        .anchor(0.5f,1f)
                        .icon(
                                BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)
                        )
                        .draggable(false)
        );
        marker.showInfoWindow();

        markers.add(marker);

    }

    public void addMarkers(){

        removeAllMarkers();

        int N = markItems.size();
        for(int i=0;i<N;i++){
            addOneMarker(new LatLng(markItems.get(i).getLatitude(),markItems.get(i).getLongitude()),
                    markItems.get(i).getPositionName(),markItems.get(i).getUserName());
        }
    }

    private void removeAllMarkers()
    {
        if(!markers.isEmpty())
        {
            for(Marker m : markers)
            {
                m.destroy();
            }
        }

        markers.clear();
    }
    //获得纬度double值
    public double getCurrentLatitude(){
        return mlocationClient.getLastKnownLocation().getLatitude();
    }

    //获得经度double值
    public double getCurrentLogitude(){
        return mlocationClient.getLastKnownLocation().getLongitude();
    }

    /**
     * 方法必须重写
     */
    @Override
    public void onResume() {
        super.onResume();
        mapView.onResume();
        init();
    }

    /**
     * 方法必须重写
     */
    @Override
    public void onPause() {
        super.onPause();
        mapView.onPause();
        deactivate();
    }

    /**
     * 方法必须重写
     */
    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }

    /**
     * 方法必须重写
     */
    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mlocationClient != null) mlocationClient.onDestroy();
        mlocationClient = null;
        mLocationOption = null;
        mapView.onDestroy();
    }

//    /**
//     * 定位成功后回调函数
//     */
//    @Override
//    public void onLocationChanged(AMapLocation amapLocation) {
//        if (mListener != null && amapLocation != null) {
//            if (amapLocation != null
//                    && amapLocation.getErrorCode() == 0) {
//                mListener.onLocationChanged(amapLocation);// 显示系统小蓝点
//            } else {
//                String errText = "定位失败," + amapLocation.getErrorCode() + ": " + amapLocation.getErrorInfo();
//                Log.e("AmapErr", errText);
//            }
//        }
//    }


    /**
     * 定位成功后回调函数
     */
    @Override
    public void onLocationChanged(AMapLocation amapLocation) {
        if (mListener != null && amapLocation != null) {
            if (amapLocation != null
                    && amapLocation.getErrorCode() == 0) {
                mListener.onLocationChanged(amapLocation);// 显示系统小蓝点

                lastLongitude = getCurrentLogitude();
                lastLatitude = getCurrentLatitude();

                counter++;
                if(counter > 90){//三分钟一次
                    new GetAroundMarks().execute();
                    counter=0;
                }


            } else {
                String errText = "定位失败," + amapLocation.getErrorCode() + ": " + amapLocation.getErrorInfo();
                Log.e("AmapErr", errText);
            }
        }
    }

    @Override
    public void onCameraChange(CameraPosition cameraPosition) {

    }

    @Override
    public void onCameraChangeFinish(CameraPosition cameraPosition) {

    }

    /**
     * 激活定位
     */
    @Override
    public void activate(OnLocationChangedListener listener) {
        mListener = listener;
        if (mlocationClient == null) {
            mlocationClient = new AMapLocationClient(getActivity());
            mLocationOption = new AMapLocationClientOption();

            mlocationClient.setLocationListener(this); //设置定位监听
            //设置为高精度定位模式
            mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
            mLocationOption.setInterval(Long.valueOf(2000L));
            //设置定位参数
            mlocationClient.setLocationOption(mLocationOption);
            // 此方法为每隔固定时间会发起一次定位请求，为了减少电量消耗或网络流量消耗，
            // 注意设置合适的定位时间的间隔（最小间隔支持为2000ms），并且在合适时间调用stopLocation()方法来取消定位请求
            // 在定位结束后，在合适的生命周期调用onDestroy()方法
            // 在单次定位情况下，定位无论成功与否，都无需调用stopLocation()方法移除请求，定位sdk内部会移除
            mlocationClient.startLocation();
        }
    }

    /**
     * 停止定位
     */
    @Override
    public void deactivate() {
        mListener = null;
        if (mlocationClient != null) {
            mlocationClient.stopLocation();
            mlocationClient.onDestroy();
        }
        mlocationClient = null;
    }


    private class GetAroundMarks extends AsyncTask<Void, Void, String> {
        private String status;
        private String info;

        @Override
        protected String doInBackground(Void... params) {
            JSONObject object = new JSONObject();
            try {
                object.put("userID", MainActivity.userId);
                object.put("longitude",lastLongitude);
                object.put("latitude",lastLatitude);
                Log.i("AroundMarks:id:", object.get("userID").toString());
                Log.i("AroundMark:longitude:", object.get("longitude").toString());
                Log.i("AroundMarks:latitude:", object.get("latitude").toString());

            } catch (JSONException e) {
                e.printStackTrace();
            }
            HttpUtil.getJsonArrayByHttp(serviceUrl, object, new HttpCallbackListener() {
                @Override
                public void onFinishGetJson(JSONObject jsonObject) {
                    if (jsonObject != null) {
                        try {
                            status = jsonObject.getString("status");
                            info = jsonObject.getString("info");
                            MarksjsonArray = jsonObject.getJSONArray("marks");
                            Log.i("AroundMarks:array:", MarksjsonArray.toString());

                            if(MarksjsonArray != null )
                            {
                                markItems.clear();

                                for(int i=0;i<MarksjsonArray.length();i++)
                                {
                                    JSONObject markObject = MarksjsonArray.getJSONObject(i);
                                    MarkItemResponse markItem = new MarkItemResponse();
                                    markItem.setContent(markObject.getString("content"));
                                    markItem.setUserName(markObject.getString("userName"));
                                    markItem.setPositionName(markObject.getString("positionName"));
                                    markItem.setLatitude(Double.valueOf(markObject.getDouble("latitude")));
                                    markItem.setLongitude(Double.valueOf(markObject.getDouble("longitude")));

                                    markItems.add(markItem);
                                }
                            }



                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    Message message= new Message();
                    if (status.equals("1") && info.equals("OK")) {
                        message.what=1;
                    }else{
                        message.what=-1;
                    }
                    handler.sendMessage(message);
                }

                @Override
                public void onFinishGetString(String response) {

                }

                @Override
                public void onError(Exception e) {
                    status="0";
                }
            });
            return null;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

    }
}

