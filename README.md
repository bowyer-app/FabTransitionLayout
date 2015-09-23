# FabTransitionLayout
Provides the Floating Action Button Transition [as specified in the Material Design Guide](http://www.google.com/design/spec/components/buttons-floating-action-button.html#buttons-floating-action-button-transitions) in a simple library.

It is strongly inspired by [fab-toolbar](https://github.com/bowyer-app/fab-toolbar)

![bottomsheet](https://github.com/bowyer-app/FabTransitionLayout/blob/master/art/bottomsheet.gif)
![bottomsheet](https://github.com/bowyer-app/FabTransitionLayout/blob/master/art/toolbar.gif)

Usage
====
### build.gradle

```
repositories {
    jcenter()
}

dependencies {
    compile 'com.bowyer.app:fabtransitionlayout:0.2.0@aar'
}
```

### Layout XML
#### BottomSheetLayout 
```
<FrameLayout xmlns:android="http://schemas.android.com/apk/res/android"
             xmlns:tools="http://schemas.android.com/tools"
             xmlns:app="http://schemas.android.com/apk/res-auto"
             android:layout_width="match_parent"
             android:layout_height="match_parent"
        >

    <com.github.ksoichiro.android.observablescrollview.ObservableListView
            android:id="@+id/list_view"
            android:layout_width="match_parent"
            android:layout_height="match_parent"
            />

    <android.support.design.widget.FloatingActionButton
            android:id="@+id/fab"
            android:layout_width="wrap_content"
            android:layout_height="wrap_content"
            android:layout_gravity="bottom|end"
            android:layout_marginBottom="32dp"
            android:layout_marginRight="32dp"
            android:src="@drawable/ic_add_white_24dp"
            app:borderWidth="0dp"
            app:fabSize="normal"
            app:rippleColor="@color/primary"
            />

    <com.bowyer.app.fabtransitionlayout.BottomSheetLayout
            android:id="@+id/bottom_sheet"
            android:layout_width="match_parent"
            android:layout_height="match_parent"
            android:layout_gravity="bottom"
            app:ft_container_gravity="center"
            app:ft_color="@color/primary">

        <ListView
                android:id="@+id/list_menu"
                android:background="@color/primary"
                android:divider="@null"
                android:layout_width="match_parent"
                android:layout_height="wrap_content"/>
    </com.bowyer.app.fabtransitionlayout.BottomSheetLayout>

</FrameLayout>

```
#### FooterLayout
```
<FrameLayout xmlns:android="http://schemas.android.com/apk/res/android"
             xmlns:tools="http://schemas.android.com/tools"
             xmlns:app="http://schemas.android.com/apk/res-auto"
             android:layout_width="match_parent"
             android:layout_height="match_parent"
             tools:context=".MainActivity">

    <com.github.ksoichiro.android.observablescrollview.ObservableListView
            android:id="@+id/list_view"
            android:layout_width="match_parent"
            android:layout_height="match_parent"
            />

    <android.support.design.widget.FloatingActionButton
            android:id="@+id/fab"
            android:layout_width="wrap_content"
            android:layout_height="wrap_content"
            android:layout_gravity="bottom|end"
            android:layout_marginBottom="32dp"
            android:layout_marginRight="32dp"
            android:src="@drawable/ic_add_white_24dp"
            app:borderWidth="0dp"
            app:fabSize="normal"
            app:rippleColor="@color/primary"
            />

    <com.bowyer.app.fabtransitionlayout.FooterLayout
            android:id="@+id/fabtoolbar"
            android:layout_width="match_parent"
            android:layout_height="wrap_content"
            android:layout_gravity="bottom"
            app:ft_container_gravity="center"
            app:ft_color="@color/primary">
        <!--add your original layout-->
    </com.bowyer.app.fabtransitionlayout.FooterLayout>
</FrameLayout>
```

### Set up

```java
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        initListView();
        //set floating button to FabToolbar
        mBottomSheetLayout.setFab(mFab);
    }
```

### Show Hide

```java
//expand FabToolbar
mBottomSheetLayout.expandFab();

//if mBottomSheetLayout is expand,mBottomSheetLayout contract. else fab slide out.
mBottomSheetLayout.slideOutFab();

//fab slide in
mBottomSheetLayout.slideInFab();


```

# Credits
This library use following libraries.
* [CircularReveal](https://github.com/ozodrukh/CircularReveal)

Feature
====
- [ ] A floating action button transforming into a single sheet of material

License
--------
```
Copyright (c) 2015 Bowyer
Released under the MIT license
http://opensource.org/licenses/mit-license.php
```
