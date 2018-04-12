# Android FloatHeartView 直播间飘心效果

 [![](https://jitpack.io/v/trc1993/AndroidFloatHeart.svg)](https://jitpack.io/#trc1993/AndroidFloatHeart)


## 效果图：

<img src="https://s19.aconvert.com/convert/p3r68-cdx67/aoii3-pc1aa.gif" width="280" height="466"/>


## 使用方法：
### Gradle
#### 1：In the project root directory build.gradle

        repositories {
          　　//Rely on the warehouse
        　　　maven { url 'https://jitpack.io' }
        　　}
        }

#### 2：The project directory build.gradle relies on the AndroidFloatHeart framework

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
        <!--这个ImageView是显示在底部 FloatHeartView可以把它当做LinearLayout-->
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


## 实现逻辑：

    
      Created by 差不多的TRC on 2018/4/11.
      
      step1: 用贝塞尔曲线好画出个二阶曲线，贝塞尔曲线很简单的，二阶曲线为例：
      个人理解，就是一根线 有两个点画在线的两侧，两个点好比两个人，用力拉这根线，
      把这个跟线拉两道弯，变成了贝塞尔曲线，几阶曲线就几个点。当然了，具体实现也很简单，
      还是二阶曲线为例：首先画笔moveTo某个点上 然后cubicTo 把两个点坐标和重点坐标写进去
      ，就完成你的贝塞尔曲线路径了；
     
      step2:曲线画完了，我在想怎么让一个图片在我规定的路径上运动，我如果知道这个Path的每个
      点的坐标就好了，然后我就可以把图片每隔一段时间更新下坐标位置，这样不就形成动画了吗，
      这时想到了PathMeasure，Android SDK提供了一个非常有用的API来帮助开发者实现这样一个
      Path路径点的坐标追踪，这个类就是PathMeasure，它可以认为是一个Path的坐标计算器。
      
      step3:哈哈，很开心，路径的坐标已经拿到手了，接下来就是每隔一段时更新坐标就好，第一反应想用Handler做个
      计时器，然后每隔几毫秒更新坐标，仔细思考下，太麻烦，而且不可控因素太多，然后想到了ValueAnimator
      这个不就是为我而生吗，哈哈，于是开始干活，在onAnimationUpdate 方法里更新坐标。嗯，效果出来了
      好不错，{自己认为还不错，哈哈} 。
      
      step4:优化动画效果，首先想到透明度，这个简单,用AnimatorSet 来个组合（透明度和坐标更新同时） so easy。
      
      step5:继续优化动画，这个路径也太单一了吧，全是一个路径上，看起来有点傻不拉几的，所以把路径那两个控制点的和终点x
      y坐标在一定范围内随机
      
      step6 努力，终有一天你得到你想要的生活!!!（>_<）
      
     
     
## Attributes

[attr.xml](floatheart/src/main/res/values/attrs.xml)

有任何问题请联系我QQ:1129440815,如果帮到你了，可不可以给个start，谢谢支持，我会持续跟新...


