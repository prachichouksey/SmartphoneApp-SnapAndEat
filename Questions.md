
### Storages in Android
- Content Provider
- SQLite
- Shared Preferneces

---

### Containers in Android
- A container is a view used to contain other views
- Android offers a collection of view classes that act as container for views.
- they decide the organization, size, and position of their children views.

---

### Important folders in Android
- **src** - Java source files associated with your project. This includes the Activity "controller" files as well as your models and helpers.
- **res** - Resource files associated with your project. All graphics, strings, layouts, and other resource files are stored in the resource file hierarchy under the res directory.
- **res/layout** - XML layout files that describe the views and layouts for each activity and for partial views such as list items.
- **res/values** - XML files which store various attribute values. These include strings.xml, dimens.xml, styles.xml, colors.xml, themes.xml, and so on.
- **res/drawable** - Here we store the various density-independent graphic assets used in our application.
- **res/drawable-hdpi** - Series of folders for density specific images to use for various resolutions.
- **res/mipmap**  - most commonly used for application icons
- **gen** - Generated Java code files, this library is for Android internal use only.
- **assets** - Uncompiled source files associated with your project; Rarely used.
- **bin** - Resulting application package files associated with your project once it’s been built.
- **libs** - Before the introduction of Gradle build system, this directory was used for any secondary libraries (jars) you might want to link to your app.

---

### Notifications
- Toast Notification
- Snack Bar

---

### Deffie Helman key exchange
- Given a prime p, a suitable elliptic curve E and a point P=(x<sub>P</sub>,y<sub>P</sub>)
- The Elliptic Curve Diffie-Hellman Key Exchange is defined by the following protocol:
- <i>Draw an arrow from Alice to bob and another bon to alice</i>

##### Alice

Choose k<sub>PrA</sub>= a     {2, 3,…, #E-1}
Compute k<sub>PubA</sub>= A = aP = (x<sub>A</sub>,y<sub>A</sub>)

Compute aB = T<sub>ab</sub>

##### Bob

Choose k<sub>PrB</sub>= a     {2, 3,…, #E-1}
Compute k<sub>PubB</sub>= B = bP = (x<sub>B</sub>,y<sub>B</sub>)

Compute bA = T<sub>ab</sub>

- Joint secret between Alice and Bob: T<sub>AB</sub> = (x<sub>AB</sub>, y<sub>AB</sub>)
- Proof for correctness:
  - Alice computes aB=a(bP)=abP
  - Bob computes bA=b(aP)=abP since group is associative
- One of the coordinates of the point T<sub>AB</sub> (usually the x-coordinate) can be used as session key (often after applying a hash function)

---

### Navigations in android
- **Temporal Navigation** - Use the back button to navigate around the app, taking you to where you were last
- **Ancestral navigation (AKA Hierarchical Navigation)** - Takes you up the app hierarchy

```
getActivity().getActionBar().setDisplayHomeAsUpEnabled(true) 
```

---


### Intents
- **Explicit (Navigation with App)** - To start a specific component ( a specific Activity Instance)

Explicit Intents have a specified a component(via setComponent(ComponentName) or setClass(Context, Class)), which provides the exact class to be run

- **Implicit (Other Apps)**
To start any component that can handle the intended action (such as “Capture a Photo” or display “Map”)

 - Build an Implicit Intent
 - Verify App to Receive the Intent
 - Start an Activity

 ---

 ### Code to create a button and on click open activity
```
  public class MainActivity extends AppCompatActivity {
    Button button;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
      button = (Button) findViewById(R.id.button);
      button.setOnClickListener(new View.OnClickListener() {
         @Override
         public void onClick(View v) {
            Intent intent = new Intent(this, NewActivity.class);
            String strName = "STRING_VALUE";
            intent.putExtra("STRING_KEY", strName);
            startActivity(intent);
         }
      });
    }

```

---

### Given textview with id, instantiate activity
```
  public class MainActivity extends AppCompatActivity {
    TextView textview;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
      setContentView(R.layout.activity_main);
      textview = (TextView) findViewById(R.id.textview);
      textview.setText("My Text");
    }
  }
```

---

### ECC formula
- In cryptography, we are interested in elliptic curves module a prime **p**
- **Definition: Elliptic Curves over prime fields**
  - The elliptic curve over Z<sub>p</sub>, p>3 is the set of all pairs (x,y)  <belongs to symbol>  Z<sub>p</sub> which fulfill
    - **y<sup>2</sup> = x<sup>3</sup> + ax + b mod p**
  - together with an imaginary point of infinity θ, where a,b  <belongs to symbol>  Zp and the condition
    - **4a<sup>3</sup>+27b<sup>2</sup> ≠ 0 mod p**
- Note that Zp = {0,1,…, p -1} is a set of integers with modulo p arithmetic
![alt text](https://github.com/prachichouksey/SmartphoneApp-SnapAndEat/blob/master/images/ECC_Formula_1.PNG)

![alt text](https://github.com/prachichouksey/SmartphoneApp-SnapAndEat/blob/master/images/Elliptic_Curve_Point_Addition_and_Doubling_Formulas.PNG)
