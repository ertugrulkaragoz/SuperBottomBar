# SuperBottomBar

## About
Spotify like android material bottom navigation bar library.

## GIF
<img src="https://github.com/ertugrulkaragoz/SuperBottomBar/blob/master/GIF/superbottombar.gif"/>

## Design Credits
All design and inspiration credits belong to [Spotify](https://play.google.com/store/apps/details?id=com.spotify.music&hl=tr).


## Setup

```gradle
allprojects {
	repositories {
		...
		maven { url 'https://jitpack.io' }
	}
}

dependencies {
        implementation 'implementation 'com.github.ertugrulkaragoz:SuperBottomBar:0.2'
}
```

## Usage

-   Create `menu.xml` in the `res/menu/` resource folder:
```xml
<?xml version="1.0" encoding="utf-8"?>
<menu xmlns:android="http://schemas.android.com/apk/res/android">
    <item
        android:id="@+id/home_super_bottom_bar"
        android:icon="@drawable/ic_baseline_home_24"
        android:title="@string/home" />

    <item
        android:id="@+id/radio_super_bottom_bar"
        android:icon="@drawable/ic_baseline_radio_24"
        android:title="@string/radio" />

    <item
        android:id="@+id/search_super_bottom_bar"
        android:icon="@drawable/ic_baseline_search_24"
        android:title="@string/search" />

    <item
        android:id="@+id/library_super_bottom_bar"
        android:icon="@drawable/ic_baseline_library_music_24"
        android:title="@string/library" />

    <item
        android:id="@+id/profile_super_bottom_bar"
        android:icon="@drawable/ic_baseline_person_24"
        android:title="@string/profile" />
</menu>
```
- Add `SuperBottomBar` in your layout:
```xml
 <me.ertugrul.lib.SuperBottomBar
        android:id="@+id/bottomBar"
        android:layout_width="match_parent"
        android:layout_height="55dp"
        app:menu="@menu/menu_bottom" />
```
-   You can customize some of the attributes:
```xml
<me.ertugrul.lib.SuperBottomBar
        android:id="@+id/bottomBar"
        android:layout_width="match_parent"
        android:layout_height="55dp"
        app:menu="@menu/menu_bottom"
        app:iconMargin=""
        app:textSize=""
        app:iconSize=""
        app:initialActiveItem=""
        app:textColor=""
        app:backgroundColor=""
        app:activeColor=""
        app:passiveColor=""
        app:pressedColor=""
        app:animationDuration=""
        app:scalePercent="" />
```

-   Get notified when `SuperBottomBar` menu item clicks by callback:
```kotlin
        bottomBar.onItemSelected = { pos ->
            Log.e("onItemSelected", "$pos")
        }

        bottomBar.onItemReselected = { pos ->
            Log.e("onItemReselected", "$pos")
        }
```
Or set a listener
```kotlin 
        bottomBar.setOnItemSelectListener(object : OnItemSelectedListener {
            override fun onItemSelect(pos: Int) {
                Log.e("selectedListener", "$pos")
            }
        })

        bottomBar.setOnItemReselectListener(object : OnItemReselectedListener {
            override fun onItemReselect(pos: Int) {
                Log.e("reSelectedListener", "$pos")
            }
        })
```


## Contact
- E-mail: ertugrulkaragoz12@gmail.com
- Twitter: [@ertugruIkaragoz](https://twitter.com/ertugruIkaragoz)

## License
```
MIT License

Copyright (c) 2021 Ertuğrul Karagöz

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
```
