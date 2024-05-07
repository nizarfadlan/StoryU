# 🎯 StoryU
StoryU is an application that displays a list of stories from friends who are studying and graduating from Dicoding. <br />

## 🌞 Day Mode
|   Story List   |  Story Detail    |   Add Story
|---	|---	|---
|  ![](https://github.com/nizarfadlan/StoryU/blob/main/art/story_list_light_mode.png)    |  ![](https://github.com/nizarfadlan/StoryU/blob/main/art/story_detail_light_mode.png)    |   ![](https://github.com/nizarfadlan/StoryU/blob/main/art/story_add_light_mode.png)
|   Add Story with Location  |   Story Map    | Setting    |
|---    |---	|---	|
|   ![](https://github.com/nizarfadlan/StoryU/blob/main/art/story_add_with_location_light_mode.png)    |   ![](https://github.com/nizarfadlan/StoryU/blob/main/art/story_map_light_mode.png)      |   ![](https://github.com/nizarfadlan/StoryU/blob/main/art/setting_light_mode.png)

<br />

## 🌚 We Support Dark Mode Too
|   Story List   |  Story Detail    |   Add Story
|---	|---	|---
|  ![](https://github.com/nizarfadlan/StoryU/blob/main/art/story_list_dark_mode.png)    |  ![](https://github.com/nizarfadlan/StoryU/blob/main/art/story_detail_dark_mode.png)    |   ![](https://github.com/nizarfadlan/StoryU/blob/main/art/story_add_dark_mode.png)
|   Add Story with Location  |   Story Map    | Setting    |
|---    |---	|---	|
|   ![](https://github.com/nizarfadlan/StoryU/blob/main/art/story_add_with_location_dark_mode.png)  |   ![](https://github.com/nizarfadlan/StoryU/blob/main/art/story_map_dark_mode.png)     |   ![](https://github.com/nizarfadlan/StoryU/blob/main/art/setting_dark_mode.png)

<br />

## 🎬 Video

https://github.com/nizarfadlan/StoryU/assets/40895148/d679e64a-a72a-4a5d-8a46-2c4db10debb1


<br />

## 📦 Package Structure
 ```
com.nizarfadlan.storyu
├── app                   		# Application class
├── data                  		# For data handling
│   ├── datasource             	# Retrieves data from various sources
│   ├── local               	# Local Persistence Database. Room (SQLite) database
│   │   ├── dao               	# Data Access Object for Room
│   │   └── room          		# Database Instance
│   ├── mediator
│   │   └── StoryRemoteMediator # Acts as a mediator between local and remote data sources
│   ├── pref               		# Datastore Setting Preference and Session
│   ├── remote               	# Handles remote data access API
│   └── repository				# Manages data resources
├── di                        	# Koin DI Modules
├── domain                    	# Core application models
├── presentation
│   ├── common                	# Contains common UI components
│   ├── ui
│   │   ├── auth               	# Auth screen
│   │   ├── base               	# Base classes for UI components.
│   │   ├── camera              # CameraX screen
│   │   ├── main               	# Main sreen (List, Detail, Add) story screen and setting screen
│   │   ├── map               	# Map screen
│   │   └── welcome          	# Welcome screen
│   └── widget                	# Widget on homescreen
└── utils                     	# Extension functions
```
<br />

## 🤗 Credits
- 🤓 Icons are from [tablericons.com](https://tablericons.com)
- 🌉 Images are from [xiaonail.com](https://xiaonail.com)
