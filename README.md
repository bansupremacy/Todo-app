# Pre-work - *Noter*

**Noter** is an android app that allows building a todo list and basic todo items management functionality including adding new items, editing and deleting an existing item.

Submitted by: **An Nguyen**

Time spent: **12** hours spent in total

## User Stories

The following **required** functionality is completed:

* [X] User can **successfully add and remove items** from the todo list
* [X] User can **tap a todo item in the list and bring up an edit screen for the todo item** and then have any changes to the text reflected in the todo list.
* [X] User can **persist todo items** and retrieve them properly on app restart

The following **optional** features are implemented:

* [ ] Persist the todo items [into SQLite](http://guides.codepath.com/android/Persisting-Data-to-the-Device#sqlite) instead of a text file
* [X] Improve style of the todo items in the list [using a custom adapter](http://guides.codepath.com/android/Using-an-ArrayAdapter-with-ListView)
* [ ] Add support for completion due dates for todo items (and display within listview item)
* [ ] Use a [DialogFragment](http://guides.codepath.com/android/Using-DialogFragment) instead of new Activity for editing items
* [ ] Add support for selecting the priority of each todo item (and display in listview item)
* [X] Tweak the style improving the UI / UX, play with colors, images or backgrounds

The following **additional** features are implemented:

* [X] Use SharedPreferences instead of a text file to persist datas
* [X] Use GSON API to convert Note objects to JSON objects and store them in SharedPreferences
* [X] Use a Handler to get the datas from SharedPreferences for better performance
* [X] Use GridView instead of ListView for a characteristic look
* [X] Use ViewHolder pattern in Adapters for better performance
* [X] Implements Parcelable in Note class to transfer its instances using Intent
* [X] Use a custom adapter for the color-choosing Spinner, which looks cool
* [X] Use startActivityForResult to get the result back from NoteEditActivity
* [ ] Make a widget displaying a note on the homescreen
* [ ] More colors!
* [ ] Caculate notes' size according to the screen's size
* [ ] Use custom fonts to get a more "hand-written" feel

## Video Walkthrough 

Here's a walkthrough of implemented user stories:

[Demo](http://i.imgur.com/Puwfiqy.mp4)

GIF created with [LiceCap](http://www.cockos.com/licecap/).

## Notes

* Tried SQLite but the CursorAdapter did not go well with current app structure.
* Tried passing Color enums using RGB values but too complicated so define colors in the resource file and get references to them
* Put a Set of JSON strings into SharedPreferences. SharedPreferences scrambled up the Set, which led to notes appearing in wrong order. Had to convert the Set into another JSON string.
* Kept getting wrong colors for note, then realized ContextCompat need to be used for getting color resources.
* Discovered [Android Asset Studio](http://romannurik.github.io/AndroidAssetStudio/index.html) which generates different layout sizes for image assets, which is super convenient.

## License

    Copyright [2016] [An Nguyen]

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

        http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.