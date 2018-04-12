# Android DashBoard 仪表盘

## 效果图：

<img src="readme_resources/gif.gif" width="280" height="466"/>


<img src="readme_resources/1.png" width="280" height="466"/> <img src="readme_resources/2.png" width="280" height="466"/> <img src="readme_resources/s3.png" width="280" height="466"/>

## 使用方法：
### Gradle
#### 1：In the project root directory build.gradle

        repositories {
          　　//Rely on the warehouse
        　　　maven { url 'https://jitpack.io' }
        　　}
        }

#### 2：The project directory build.gradle relies on the DashBoard framework

        implementation 'com.github.trc1993:AndroidFloatHeart:1.0.0'


### 在xml您可以这样使用它


    <com.trc.floatheart.FloatHeartView
        android:id="@+id/fhv"
        android:layout_width="match_parent"
        android:layout_height="match_parent"
        android:background="#00000000"
        app:floatIconHeight="10dp"
        app:floatIconWidth="10dp"
        app:tagIconSrc="@mipmap/heart3"
        app:tagIconWidth="10dp">
        <!--这个ImageView是显示在底部-->
        <ImageView
            android:id="@+id/tagIv"
            android:layout_width="30dp"
            android:layout_height="30dp"
            android:layout_marginLeft="180dp"
            android:layout_marginTop="500dp"
            android:alpha="1"
            android:src="@mipmap/heart3" />
    </com.trc.floatheart.FloatHeartView>


### 代码中调用它

   /***
     *
     * @param startLocationIv 就是包裹这个view下的ImageView，需要他的位置
     * @param redId 飘出来的资源图片
     */
        addFloatHeart(ImageView startLocationIv, int redId)


## Attributes

[attr.xml](dashboardview/src/main/res/values/attrs.xml)

有任何问题请联系我QQ:1129440815,如果帮到你了，可不可以给个start，谢谢支持，我会持续跟新...


