## 레포지토리 설명
<p>Prography-9th AOS Assignment</p>
<p>Unsplash API 활용 사진 앱</p>

<br>

## 폴더 구조

```markdown
📂 com.weave.project
┣ 📂 data
┃ ┣ 📂 local
┃ ┗ 📂 remote
┃   ┣ 📂api
┃   ┗ 📂dto
┣ 📂 model
┣ 📂 repository
┣ 📂 util
┣ 📂 view
┃ ┣ 📂 adapter
┃ ┗ 📂 base
┗ 📂 viewmodel

```

<br>

## 화면 별 설명

### 공통
RoomDB / Retrofit / ViewModel / loggin-interceptor / fragment-ktx

<br>

### 메인 페이지
**북마크 세션**
<p>RoomDB에 저장된(북마크 된) 이미지를 Horizontal RecyclerView로 보임</p>
<p>DiffUtil를 통해 아이템 추가 및 제거 진행</p>
<p>북마크한 사진이 없다면 북마크 세션 숨김</p>

<br>

**최신 이미지 세션**
<p>StaggeredGridLayoutManager를 사용해 뷰의 이미지를 이미지 크기에 맞춰 조절되게 함</p>
<p>onItemClickListener를 작성해 클릭 한 사진의 Id를 받아 상세 정보 다이얼로그에 넘길 수 있도록 함</p>
<p>최하단 아이템으로 부터 특정 값 만큼 떨어진 아이템이 로드되면 새로운 아이템을 호출하고 추가 (무한 스크롤)</p>
<p>DiffUtil를 통해 아이템 추가 진행</p>

<br>

### 랜덤 포토 페이지
<p>CardStackView 라이브러리 사용해 구현함 (옵션 2)</p>
<p>좌로 스와이프 시 다음 아이템으로 넘어감</p>
<p>우로 스와이프 시 북마크 추가 후 다음 아이템으로 넘어감</p>
<p>북마크 버튼 클릭 시 북마크 추가 후 다음 아이템으로 넘어감</p>
<p>Info 버튼 클릭 시 상세 정보 다이얼로그 Show</p>

<br>

### 포토 디테일
<p>DialogFragment로 구현</p>
<p>ViewModel & Databinding 사용해 호출한 사진 정보 데이터가 뷰에 보여지도록 함</p>
<p>이미지는 가로사이즈 고정 / 사진 비율에 따라 높이 가변적으로</p>
<p>북마크 클릭 시 북마크 여부를 체크 후(이미 북마크됨 -> 북마크 해제 / 북마크 X -> 북마크 추가)</p>
<p>북마크 시 버튼 apacity 30% </p>
<p>다운로드 클릭 시 저장소 접근 권한 체크 후 요청</p>
<p>거부 후 재클릭 시 -> 설정으로 이동시켜 수동 변경 유도</p>
<p>허용 후 재클릭 시 -> DownloadManager 사용해 다운로드 진행</p>

<br>

## 사용 라이브러리
```gradle

    implementation 'androidx.core:core-ktx:1.12.0'
    implementation 'androidx.appcompat:appcompat:1.6.1'
    implementation 'com.google.android.material:material:1.11.0'
    implementation 'androidx.constraintlayout:constraintlayout:2.1.4'
    testImplementation 'junit:junit:4.13.2'
    androidTestImplementation 'androidx.test.ext:junit:1.1.5'
    androidTestImplementation 'androidx.test.espresso:espresso-core:3.5.1'

    implementation "com.squareup.retrofit2:retrofit:2.9.0"
    implementation "com.squareup.retrofit2:converter-gson:2.9.0"
    implementation 'com.squareup.okhttp3:logging-interceptor:4.9.3'

    implementation "com.github.bumptech.glide:glide:4.16.0"
    kapt "com.github.bumptech.glide:compiler:4.16.0"

    implementation "androidx.activity:activity-ktx:1.8.2"
    implementation "androidx.fragment:fragment-ktx:1.6.2"

    implementation 'androidx.room:room-runtime:2.6.1'
    kapt 'androidx.room:room-compiler:2.6.1'

    implementation "com.github.yuyakaido:CardStackView:v2.3.4"

    implementation("androidx.room:room-runtime:2.6.1")
    annotationProcessor("androidx.room:room-compiler:2.6.1")
    kapt("androidx.room:room-compiler:2.6.1")
    implementation("androidx.room:room-ktx:2.6.1")

```
