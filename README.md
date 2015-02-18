# easyrecycler
provide fading action bar with recyclerview

![alt text](https://api.monosnap.com/rpc/file/download?id=8ghh7zA5Nzuj0lGwkoEvEcI3pOZ9o8)

##### Gradle
```groovy
dependencies {
   compile 'pro.useit.recyclerview:easyrecycler:1.0.7'
   compile 'com.android.support:cardview-v7:21.0.3'//cause requered RecyclerView and Layout Managers
}
````

#####Use ParallaxRecyclerAdapter
[see example][1]
[1]:https://github.com/rovkinmax/easyrecycler/blob/master/app/src/main/java/pro/useit/paralaxrecycleradapter/ExampleAdapter.java

#####Use SpacesItemDecoration
similar spacing between the elements even in the grid
```java
int spanCount = 3;
int dividerWidth = 10;
final SpacesItemDecoration itemDecoration = new SpacesItemDecoration(deviderWidth, spanCount);
itemDecoration.setIgnoreFirst(adapter.isEnableHeader());
recyclerView.addItemDecoration(itemDecoration);
````
#####Use GridLayoutManager with header
```java
ParallaxRecyclerAdapter adapter = //some implements of ParallaxRecyclerAdapter
HeaderGridLayoutManager layoutManager = new HeaderGridLayoutManager(adapter, spanCount, HeaderGridLayoutManager.VERTICAL, false);
recyclerView.setLayoutManager(layoutManager);
````
License
-----
Copyright (c) 2015 Rovkin Max

Licensed under the Apache License, Version 2.0
