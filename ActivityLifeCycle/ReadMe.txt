CS646-Assignment #1                                                                       Red Id:-818630469

Issues
1. When you run your app and rotate the device/emulator are the method displayed in the
TextView consistent with methods called in the log? If not what would you have to do to
make them consistent?

This app displays the methods called during the Activity Lifecycle in a TextView.Name of the each method called is 
appended to the end of the TextView and also displayed in the Log(Logcat).
when the emulator/device rotates the methods displayed in the TextView is not in consistent with the methods
in the log. 

Whenever there is a change in screen orientation previous activity gets destroyed and a new activity is created( with 
alternative layout if present).when the activity is about to stop onSaveInstanceState() is called to save the state 
of the activity(default information) in case if the activity has to be recreated.This data is passed in the form of 
bundle to oncreate() and onRestoreInstanceState() to create new activity and retain previous state.

In this app the contents of the TextView has to be saved explicitly in the onSaveInstanceState() and passed to
oncreate() and onRestoreInstanceState(),so when the new activity is created ,onCreate(Bundle savedInstanceState) 
 receives the saved data bundle and restores the contents of the TextView .

