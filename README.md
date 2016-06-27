Bling Bling Click Effect of Android
==========

Just for fun. 

Enable BlingBling click effect in Activity's onCreate function without modifying layout xml:

```java
	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        
        // Enable BlingBling Effect
        BlingBlingClickHelper.enableBlingBlingClick(this);
    }
    
```

![Alt text](screenshots/1.jpeg)

![Alt text](screenshots/3.jpeg)



