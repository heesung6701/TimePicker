[![](https://jitpack.io/v/heesung6701/TimePicker.svg)](https://jitpack.io/#heesung6701/TimePicker)

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
   // do something
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

[TimeItemArrayPublisher 자세히 보기](https://github.com/heesung6701/TimePicker/wiki/TimeItemArrayPublisher)

### Attributes

> Clock 관련 속성
- [clockSize](https://github.com/heesung6701/TimePicker/wiki/clockSize)
- [clockPadding](https://github.com/heesung6701/TimePicker/wiki/clockPadding)
- [clockBorderColor](https://github.com/heesung6701/TimePicker/wiki/clockBorderColor)
- [clockBorderWidth](https://github.com/heesung6701/TimePicker/wiki/clockBorderWidth)
- [clockColor](https://github.com/heesung6701/TimePicker/wiki/clockColor)
- [centerImage](https://github.com/heesung6701/TimePicker/wiki/centerImage)

> Time 관련 속성
- [timeTextColor](https://github.com/heesung6701/TimePicker/wiki/timeTextColor)
- [timeSelectedTextColor](https://github.com/heesung6701/TimePicker/wiki/timeSelectedTextColor)
- [timeBackgroundColor](https://github.com/heesung6701/TimePicker/wiki/timeBackgroundColor)

### Version Log

- v0.0.1

> 기초적인 기능 구현 후 라이브러리 화 시도

- v0.0.2

> clockBorderWidth 추가
> 진행중

### Project Architecture

### Project Structure

#### [TimeItemArrayPublisher](https://github.com/heesung6701/TimePicker/wiki/TimeItemArrayPublisher)

#### [Calculation Process](https://github.com/heesung6701/TimePicker/wiki/Calculation-Process)


### Plans

- Time 범위 지정 가능
- ImageResource 설정
- ImageDrawable 설정
- time item을 layout으로 지정할 수 있도록 구현
- Adapter 생성
- Extends a CircularListView
