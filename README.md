# CurtainCall
A theater & musical networking platform that shares the emotions on stage

<img src=https://github.com/user-attachments/assets/8844abc8-504c-45da-9529-a6f6cf905bb9 height=600/>

<br>

## Download
Go to the [Release](https://play.google.com/store/apps/details?id=com.cmc.curtaincall&hl=ko&gl=US) to download the latest APK.

<br>

## Tech stack & Open-source libraries
- Min SDK 26
- Kotlin based, Coroutines + Flow for asynchronous.
- Jetpack
    - Compose
    - Lifecycle
    - ViewModel
    - Room
    - Hilt
    - DataStore
    - Navigation
    - Paging
- Coil
- Retrofit2 & OkHttp3 & Gson
- 3rd party library
    - Timber
    - Naver-map
    - kakao login
    - naver login
    - [calendar compose](https://github.com/kizitonwose/Calendar)
- firebase
- getstream chatting sdk

<br>

## Architecture
Curtaincall is based on a clean architecture, and the presentation applies the MVI + Redux pattern.
![서비스 구조도 Android 3](https://github.com/user-attachments/assets/0807cf54-7eed-4aeb-8013-25ce9f61c17d)

<br>

## Modularization
![서비스 구조도 Android 02](https://github.com/user-attachments/assets/7a34e0c6-0016-4697-8be5-34eb1083aaeb)

- Dependency management : Kotlin Convention Plugin
- App : Entry point & splash & onboarding
- Core
    - base : base class
    - local : local db, disk file
    - network : api
- Common
    - design : compose ui component
    - utility : every extension
- Data : datasource & business logic
- Domain : repositoryImpl & eneities
- feature
    - auth : login & signup ui
    - home : home ui
    - livetalk : livetalk ui
    - mypage : mypage ui
    - party : party ui
    - show : show ui
 
<br>

# License
```
Licensed under the Apache License, Version 2.0 (the "License");
 you may not use this file except in compliance with the License.
 You may obtain a copy of the License at

     http://www.apache.org/licenses/LICENSE-2.0

 Unless required by applicable law or agreed to in writing, software
 distributed under the License is distributed on an "AS IS" BASIS,
 WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 See the License for the specific language governing permissions and
 limitations under the License.
```


  

