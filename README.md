Bling Bling Click Effect of Android
==========

Just for fun. 

Enable BlingBling Click Effect By (No need to change your layout):

```
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



