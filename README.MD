[![Release](https://jitpack.io/v/heesung6701/TimePicker.svg)]
(https://jitpack.io/v/heesung6701/TimePicker.svg)

# TimePicker

## Description
TimePickerView는 시간 선택 UI를 위한 View입니다.
드래그를 이용해서 원하는 시간을 손쉽게 선택할 수 있습니다.

## Features
- drag를 이요한 선택 (보간법 적용)
- Clock 종료 날자 조절 가능
- 각도를 기준으로 계산
- 호의 길이가  작은 영역은 터치 무효

## SetUp

Add the JitPack repository in your build.gradle (top level module):
```gradle
allprojects {
    repositories {
        jcenter()
        maven { url "https://jitpack.io" }
    }
}
```

And add next dependencies in the build.gradle of the module:
```gradle
dependencies {
    implementation 'com.github.heesung6701:TimePicker:0.0.1'
}
```


### Screenshots

![스크린샷 2020-03-02 오후 6 44 54](https://user-images.githubusercontent.com/9072200/75664719-4898d400-5cb6-11ea-8bd5-83e37389686f.png)

### Use

#### 1. xml 에 View 추가
```
<com.github.heesung6701.timepicker.library.TimePickerView
  android:id="@+id/time_picker"
  android:layout_width="match_parent"
  android:layout_height="match_parent"
/>
```
[속성 더 보기](#attributes)



#### 2. 데이터 읽기/쓰기

선택된 시간의 정보는 TimePickerView의 `timeItemArray`에 저장되어 있다.
[timeItemArray 자세히 보기](#project-structure)
```
time_picker.timeItemArray[0].selected = true
```

#### 옵저버를 이용한 데이터 읽기

1. Observer<Array<TimeItem>> Interface 추가
```
...
class MainActivity : AppCompatActivity(), Observer<Array<TimeItem>>
...
override fun update(item: Array<TimeItem>) {
   // TODO
}
```

2. timeItemArray에 Observer를 추가한다.
```
time_picker.timeItemArray.add(this)
```

update 함수는  **배열이 값이 변경**되거나 벼열의 값인 **Item의 속성이 변경**되면 호출된다.

>   :bell: update 호출되는 경우
> time_picker.timeItemArray[0].selected = true
> time_picker.timeItemArray[0] = TimeItem(false)

 하지만 배열 자체가 변경되는 경우는 호출되지 않는다.
>   :no_bell: update 호출 안되는 경우
> time_picker.timeItemArray = TimeItemArrayPublisher(24) { TimeItem.makeDefault() }

[TimeItemArrayPublisher 자세히 보기](#timeitemarraypublisher)

### Attributes

#### clockSize

Clock의 크기를 지정한다.

```
app:clockSize="24"
```

|  |  |
|:--|:--|
|name|clockSize|
|Type| Int|
|Default| 24|


> clockSize: 24

![FullImage](https://user-images.githubusercontent.com/9072200/75664738-4f274b80-5cb6-11ea-925d-1128e94891f0.png)

> clockSize: 12
![스크린샷 2020-03-02 오후 6 45 36](https://user-images.githubusercontent.com/9072200/75664723-49316a80-5cb6-11ea-910d-398ea7688a91.png)

#### clockPadding

Clock 외부 여백을 지정한다.

```
app:clockPadding="50"
```

|  |  |
|:--|:--|
|Name|clockPadding|
|Type| Float|
|Default| 50.0f|

> clockPadding: 50.0f
![Screenshot_1583143806](https://user-images.githubusercontent.com/9072200/75666619-77fd1000-5cb9-11ea-8426-f4c70c0e7467.png)

> clockPadding: 0.0f
![Screenshot_1583143543](https://user-images.githubusercontent.com/9072200/75666336-f6a57d80-5cb8-11ea-954c-acf627c307fa.png)


#### timeTextColor

시간 텍스트 색상을 지정한다

```
app:timeTextColor
```

```
app:timeTextColor="#F6BB43"
```
|  |  |
|:--|:--|
|Name|timeTextColor|
|Type| color|
|Default| GRAY |

![Screenshot_1583144107](https://user-images.githubusercontent.com/9072200/75667099-5ea89380-5cba-11ea-9897-e75f5fcfdeff.png)

> timeTextColor="#F6BB43"
![Screenshot_1583144084](https://user-images.githubusercontent.com/9072200/75666958-1ab58e80-5cba-11ea-8e3d-a69aa5eff113.png)


> timeTextColor="#FFF700"
![Screenshot_1583144186](https://user-images.githubusercontent.com/9072200/75667072-54869500-5cba-11ea-86b7-ffbac034f299.png)



#### timeSelectedTextColor

선택시 텍스트(시간) 색상을 지정한다

```
app:timeSelectedTextColor="#F6BB43"
```

|  |  |
|:--|:--|
|Name|timeSelectedTextColor|
|Type| color|
|Default| WHITE |

![Screenshot_1583144465](https://user-images.githubusercontent.com/9072200/75667412-f7d7aa00-5cba-11ea-8fa3-00455363ee7a.png)

> timeSelectedColor="#F6BB43"
![Screenshot_1583144414](https://user-images.githubusercontent.com/9072200/75667347-dc6c9f00-5cba-11ea-98cc-f1f2fb3db247.png)

> timeSelectedColor="#FF7F00"
![Screenshot_1583144403](https://user-images.githubusercontent.com/9072200/75667340-da0a4500-5cba-11ea-9f5f-9d1807c3b82f.png)


#### timeBackgroundColor

선택시 시간 배경 색상을 지정한다

```
app:timeBackgroundColor="#F6BB43"
```

|  |  |
|:--|:--|
|Name|timeBackgroundColor|
|Type| color|
|Default| GRAY |

> timeBackgroundColor = "#F6BB43"
![Screenshot_1583144601](https://user-images.githubusercontent.com/9072200/75667612-4c7b2500-5cbb-11ea-95bc-8723a6c2e21f.png)

> timeBackgroundColor = "#FF7F00"
![Screenshot_1583144594](https://user-images.githubusercontent.com/9072200/75667607-4a18cb00-5cbb-11ea-8b00-e54742e0b5ef.png)



#### clockBorderColor

시계의 테두리 색상을 지정한다.

```
app:clockBorderColor="#F6BB43"
```

|  |  |
|:--|:--|
|Name|clockBorderColor|
|Type| color|
|Default| GRAY |

> clockBorderColor="#F6BB43"
![Screenshot_1583144822](https://user-images.githubusercontent.com/9072200/75668075-12f6e980-5cbc-11ea-83b1-e3be9d491bec.png)

> clockBorderColor="#FF7F00"
![Screenshot_1583144842](https://user-images.githubusercontent.com/9072200/75668077-14281680-5cbc-11ea-8ed9-0c34ddb9e842.png)

#### clockColor

시계의 배경 색상을 지정한다.

```
app:clockColor="#F6BB43"
```

|  |  |
|:--|:--|
|Name|clockColor|
|Type| color|
|Default| WHITE |

> clockColor="#FF7F00"
![Screenshot_1583144967](https://user-images.githubusercontent.com/9072200/75668129-27d37d00-5cbc-11ea-8e05-804f921efa1f.png)

> clockColor="#F6BB43"
![Screenshot_1583144961](https://user-images.githubusercontent.com/9072200/75668120-23a75f80-5cbc-11ea-8b53-2ad24d3558b1.png)


#### centerImage

시계 중앙의 이미지를 추가한다.

```
app:centerImage="@drawable/ic_search"
app:centerImageWidth="100dp"
app:centerImageHeight="100dp"
```

|  |  | | |
|:--|:--|:--|:--|
|Name|centerImage|centerImageWidth| centerImageHeight|
|Type| reference| dimension | dimension |
|Default| NONE | 200.0f | 200.0f |

![Screenshot_1583145551](https://user-images.githubusercontent.com/9072200/75668982-83ead100-5cbd-11ea-9260-d71ca082ba60.png)

> centerImage="@drawable/ic_search"
> app:centerImageWidth="50dp"
app:centerImageHeight="50dp"
![Screenshot_1583145541](https://user-images.githubusercontent.com/9072200/75668974-7fbeb380-5cbd-11ea-8c8f-a2fc0c3fdcbd.png)


> app:centerImage="@drawable/ic_launcher_foreground"
app:centerImageWidth="300dp"
app:centerImageHeight="300dp"
![Screenshot_1583145615](https://user-images.githubusercontent.com/9072200/75669040-a4b32680-5cbd-11ea-83f4-5cc6457c39b4.png)


### Version Log

- v0.0.1
기초적인 기능 구현 후 라이브러리 화 시도

### Project Architecture

### Project Structure

#### TimeItemArrayPublisher
TimeItemArrayPublisher는 Array<TimeItem>을 가지고 있는 Publisher이다.
```
class TimeItemArrayPublisher(size: Int, init: (Int) -> TimeItem) : Publisher<Array<TimeItem>> {

private val array = Array<TimeItem>(size) { init(it) }
...
```
하지만 operator를 이용해서 배열과 같이 접근이 가능하다.
```
operator fun set(index: Int, element: TimeItem) {
    array[index] = element
    notifyObserver()
}

operator fun get(index: Int): TimeItem {
    return array[index]
}
```
여기서 주의할 점은 setter 함수를 호출하면 내부의 배열의 값을 바꾸면서 Observer에게 이 사실을 알린다.


```
class TimeItemArrayPublisher(...) : Publisher<Array<TimeItem>>, Observer<TimeItem>  {
```
TimeItemArrayPublisher는 배열의 요소인 TimeItem의 속성이 변경되는 것 또한 관측 하고 있다.  따라서 **TimeItemArrayPublisher는 Array을 제공하는 Publisher**이면서 **TimeItem을 관측하는 Observer**이다.

#### Calculation Process

1. 입력된 좌표를 이용해서 Angle을 계산한다.
2. 계산된 Angle을 sampling 해서 hour를 구한다.
3. 이전 값을 이용해서 빠르게 드래그 할 경우 놓치게 되는 시간 값을 인식한다.
4. 데이터를 업데이트 한다.

### Plans

- Time 범위 지정 가능
- clock Border Stroke 설정 가능
- ImageResource 설정
- ImageDrawable 설정
- time item을 layout으로 지정할 수 있도록 구현
- Adapter 생성
- Extends a CircularListView
