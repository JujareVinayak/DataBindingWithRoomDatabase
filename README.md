# DataBindingWithRoomDatabase

Here is the demo project for Room Database with some android jetpack components(DataBinding, LiveData, ViewModel).

1. Room Database is an alternative to SQLite. It is very easy and helps to remove a lot of boiler plate code. We can perform CRUD operations with just Annotations rather than writing a lot of code.
2. DataBinding is very helpful to assign data to the widgets(TextView,EditText,Button) directly in the layout file. This removes the need to call any of the Java/Kotlin code.

Without DataBinding:
TextView textView = findViewById(R.id.sample_text);
textView.setText(viewModel.getUserName());

With DataBinding:

In TextView tag of layout file add below line.
    android:text="@{viewmodel.userName}""
    
3. LiveData and ViewModel are used to provide MVVM artitechture. To understand LiveData and ViewModel refer below MVVM tutorial .
Link: https://github.com/JujareVinayak/MVVM
