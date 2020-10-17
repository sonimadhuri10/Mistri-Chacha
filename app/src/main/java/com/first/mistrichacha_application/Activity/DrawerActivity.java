package com.first.mistrichacha_application.Activity;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.first.mistrichacha_application.Comman.ConnectionDetector;
import com.first.mistrichacha_application.Comman.SessionManagment;
import com.first.mistrichacha_application.DatabaseManager.DatabaseHelper;
import com.first.mistrichacha_application.Fragment.Application.AboutUs_Fragment;
import com.first.mistrichacha_application.Fragment.Application.ContactUsFragment;
import com.first.mistrichacha_application.Fragment.Application.Faq_Fragment;
import com.first.mistrichacha_application.Fragment.Application.PrivacyPolicy_fragment;
import com.first.mistrichacha_application.Fragment.Application.TermsAndCondition_fragment;
import com.first.mistrichacha_application.Fragment.Drawer.AllStoreFragment;
import com.first.mistrichacha_application.Fragment.Drawer.CategoryFragment;
import com.first.mistrichacha_application.Fragment.Drawer.CoupanFragment;
import com.first.mistrichacha_application.Fragment.Drawer.DiscountProducts;
import com.first.mistrichacha_application.Fragment.Drawer.FeatureProduct;
import com.first.mistrichacha_application.Fragment.Drawer.LatestProducts;
import com.first.mistrichacha_application.Fragment.Drawer.TrendingProducts;
import com.first.mistrichacha_application.Fragment.Drawer.UserProfileFragment;
import com.first.mistrichacha_application.Fragment.Drawer.WholeSaleFragment;
import com.first.mistrichacha_application.Fragment.Drawer.dashboard_Fragment;
import com.first.mistrichacha_application.Fragment.User.MyCartFragment;
import com.first.mistrichacha_application.Fragment.User.MyOrdersfargment;
import com.first.mistrichacha_application.Fragment.User.NotificationFragment;
import com.first.mistrichacha_application.Model.CartModel;
import com.first.mistrichacha_application.Model.SignupModel;
import com.first.mistrichacha_application.Networkmanager.APIClient;
import com.first.mistrichacha_application.Networkmanager.APIInterface;
import com.first.mistrichacha_application.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;


public class DrawerActivity extends AppCompatActivity implements
        NavigationView.OnNavigationItemSelectedListener,
        View.OnClickListener {

    DrawerLayout drawer;
    Toolbar toolbar;
    BottomNavigationView bottomNavigationView;
    static int coutn1 = 0;
    ImageView imgCart, imgNotification, imgLogout;
    SessionManagment sd;
    ConnectionDetector cd;
    APIInterface apiInterface;
    TextView tvName, tvEmail, tvWelcome, tvCartCount;
    CircleImageView circleImageView;
    LinearLayout llSingup, llLogin;
    String name = "", email = "", photo = "" , mobile="",address="",
    city="",zip="",state="",country="",flat_no="",building="";;
    String fragmentIntent = null;
    DatabaseHelper mydb;
    ArrayList<CartModel> al = new ArrayList<>();
    private static final int PROFILE_UPDATE_REQUEST = 80;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.drawer_activity);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open,
                R.string.navigation_drawer_close);
        toolbar.setNavigationIcon(R.drawable.menuicontoolbar);
        TextView header = findViewById(R.id.pg2_header_footer);
        header.setText("Mistri Chacha");

        imgCart = (ImageView) findViewById(R.id.imgCart);
        imgNotification = (ImageView) findViewById(R.id.imgNotification);
        imgLogout = (ImageView) findViewById(R.id.imgLogout);
        tvCartCount = (TextView) findViewById(R.id.tvCartCount);


        // HEADER SECTION
        View headerView = navigationView.inflateHeaderView(R.layout.nav_header_newly__updated_);
        tvName = (TextView) headerView.findViewById(R.id.tvName);
        tvEmail = (TextView) headerView.findViewById(R.id.tvEmail);
        tvWelcome = (TextView) headerView.findViewById(R.id.tvWelcome);
        circleImageView = (CircleImageView) headerView.findViewById(R.id.imgUser);
        llLogin = (LinearLayout) headerView.findViewById(R.id.ll_Login);
        llSingup = (LinearLayout) headerView.findViewById(R.id.llRegister);

        apiInterface = APIClient.getClient().create(APIInterface.class);
        sd = new SessionManagment(this);
        cd = new ConnectionDetector(this);
        mydb = new DatabaseHelper(DrawerActivity.this);

        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);

        bottomNavigationView=(BottomNavigationView)findViewById(R.id.bottom_navigation);

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.container, new dashboard_Fragment()).commit();

        imgCart.setOnClickListener(this);
        imgNotification.setOnClickListener(this);
        imgLogout.setOnClickListener(this);
        llSingup.setOnClickListener(this);
        llLogin.setOnClickListener(this);
        circleImageView.setOnClickListener(this);

        if(sd.getLOGIN_STATUS().equals("true")){
            retro();
            calculation();
        }


        fragmentIntent = getIntent().getStringExtra("fragment");
        if (fragmentIntent != null) {
            if (fragmentIntent.equalsIgnoreCase("notification")) {
                FragmentManager fragmentManager1 = getSupportFragmentManager();
                FragmentTransaction fragmentTransaction1 = fragmentManager1.beginTransaction();
                fragmentTransaction1.setCustomAnimations(R.anim.fadein, R.anim.fade_out);
                fragmentTransaction1.replace(R.id.container, new NotificationFragment());
                fragmentTransaction1.commit();
            }else if(fragmentIntent.equalsIgnoreCase("cart")){
                FragmentManager fragmentManagercart = getSupportFragmentManager();
                FragmentTransaction fragmentTransactioncart = fragmentManagercart.beginTransaction();
                fragmentTransactioncart.setCustomAnimations(R.anim.fadein, R.anim.fade_out);
                fragmentTransactioncart.replace(R.id.container, new MyCartFragment());
                fragmentTransactioncart.commit();
            }else if(fragmentIntent.equalsIgnoreCase("orderdetails")){
               /* UserProfileFragment fragment = new UserProfileFragment();
                Bundle bundle = new Bundle();
                FragmentManager fragmentManager1 = getSupportFragmentManager();
                FragmentTransaction fragmentTransaction1 = fragmentManager1.beginTransaction();
                bundle.putString("order","order");
                fragment.setArguments(bundle);
                fragmentTransaction1.setCustomAnimations(R.anim.fadein, R.anim.fade_out);
                fragmentTransaction1.replace(R.id.container, fragment);
                fragmentTransaction1.addToBackStack(null);
                fragmentTransaction1.commit();*/
                FragmentManager fragmentManagerpro = getSupportFragmentManager();
                FragmentTransaction fragmentTransactionpro = fragmentManagerpro.beginTransaction();
                fragmentTransactionpro.setCustomAnimations(R.anim.fadein, R.anim.fade_out);
                fragmentTransactionpro.replace(R.id.container, new MyOrdersfargment());
                fragmentTransactionpro.commit();
            }
        }

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.navigation_me:
                        if(sd.getLOGIN_STATUS().equals("skip")){
                            showdialog();
                        }else{
                            drawer.closeDrawer(Gravity.LEFT);
                            UserProfileFragment fragment = new UserProfileFragment();
                            Bundle bundle = new Bundle();
                            FragmentManager fragmentManager1 = getSupportFragmentManager();
                            FragmentTransaction fragmentTransaction1 = fragmentManager1.beginTransaction();
                            bundle.putString("order","yes");
                            fragment.setArguments(bundle);
                            fragmentTransaction1.setCustomAnimations(R.anim.fadein, R.anim.fade_out);
                            fragmentTransaction1.replace(R.id.container, fragment);
                            fragmentTransaction1.addToBackStack(null);
                            fragmentTransaction1.commit();
                        }
                        break;
                    case R.id.navigation_home:
                        FragmentManager fragmentManager = getSupportFragmentManager();
                        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                        fragmentTransaction.setCustomAnimations(R.anim.fadein, R.anim.fade_out);
                        fragmentTransaction.replace(R.id.container, new dashboard_Fragment());
                        fragmentTransaction.commit();
                        break ;
                    case R.id.navigation_store:
                        drawer.closeDrawer(Gravity.LEFT);
                        FragmentManager fragmentManager1 = getSupportFragmentManager();
                        FragmentTransaction fragmentTransaction1 = fragmentManager1.beginTransaction();
                        fragmentTransaction1.setCustomAnimations(R.anim.fadein, R.anim.fade_out);
                        fragmentTransaction1.replace(R.id.container, new AllStoreFragment());
                        fragmentTransaction1.commit();
                        break;
                    case R.id.navigation_cart:
                        FragmentManager fragmentManagercart = getSupportFragmentManager();
                        FragmentTransaction fragmentTransactioncart = fragmentManagercart.beginTransaction();
                        fragmentTransactioncart.setCustomAnimations(R.anim.fadein, R.anim.fade_out);
                        fragmentTransactioncart.replace(R.id.container, new MyCartFragment());
                        fragmentTransactioncart.commit();
                        break;
                    case R.id.navigation_cate:
                        FragmentManager fragmentManagerc = getSupportFragmentManager();
                        FragmentTransaction fragmentTransactionc = fragmentManagerc.beginTransaction();
                        fragmentTransactionc.setCustomAnimations(R.anim.fadein, R.anim.fade_out);
                        fragmentTransactionc.replace(R.id.container, new CategoryFragment());
                        fragmentTransactionc.commit();
                        break;
                    default:
                        break;

                }
                return true;
            }
        });

    }

    public void calculation() {
        al = mydb.getAllcartProducts();
        int totalPrice = 0, quan = 0;
        for (int i = 0; i < al.size(); i++) {
            totalPrice += al.get(i).getTotal();
            quan += al.get(i).getQuan();
        }
        tvCartCount.setText(String.valueOf(quan));
    }

    @Override
    protected void onStart() {
        super.onStart();
        if(sd.getLOGIN_STATUS().equals("true")){
            calculation();
        }

    }

    @Override
    public void onBackPressed() {
            if (coutn1 == 1) {
                FragmentManager fragmentManager = getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.setCustomAnimations(R.anim.fadein, R.anim.fade_out);
                fragmentTransaction.replace(R.id.container, new dashboard_Fragment());
                fragmentTransaction.commit();
                coutn1++;
            } else {
                int backStackCount = getSupportFragmentManager().getBackStackEntryCount();
                if (backStackCount == 0) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(DrawerActivity.this);
                    ViewGroup viewGroup = findViewById(android.R.id.content);
                    View dialogView = LayoutInflater.from(DrawerActivity.this).inflate(R.layout.custom_dialog_layout, viewGroup, false);

                    builder.setView(dialogView);
                    final AlertDialog alertDialog = builder.create();
                    alertDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
                    alertDialog.show();

                    TextView tvYes , tvNo ,tvheader ;
                    tvYes=(TextView)dialogView.findViewById(R.id.tvYes);
                    tvNo=(TextView)dialogView.findViewById(R.id.tvNo);
                    tvheader=(TextView)dialogView.findViewById(R.id.tvHeader);

                    tvheader.setText("Are you sure you want to exit this app ?");
                    tvYes.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent start = new Intent(Intent.ACTION_MAIN);
                            start.addCategory(Intent.CATEGORY_HOME);
                            start.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            start.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(start);
                            finish();
                        }
                    });

                    tvNo.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            alertDialog.dismiss();
                        }
                    });

                } else {
                    super.onBackPressed();
                }

            }

    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_home) {
           drawer.closeDrawer(Gravity.LEFT);
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.setCustomAnimations(R.anim.fadein, R.anim.fade_out);
            fragmentTransaction.replace(R.id.container, new dashboard_Fragment());
            fragmentTransaction.commit();
        }else if(id == R.id.nav_category){
            drawer.closeDrawer(Gravity.LEFT);
            FragmentManager fragmentManager1 = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction1 = fragmentManager1.beginTransaction();
            fragmentTransaction1.setCustomAnimations(R.anim.fadein, R.anim.fade_out);
            fragmentTransaction1.replace(R.id.container, new CategoryFragment());
            fragmentTransaction1.commit();
        } else if (id == R.id.nav_latest) {
            drawer.closeDrawer(Gravity.LEFT);
            FragmentManager fragmentManager1 = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction1 = fragmentManager1.beginTransaction();
            fragmentTransaction1.setCustomAnimations(R.anim.fadein, R.anim.fade_out);
            fragmentTransaction1.replace(R.id.container, new LatestProducts());
            fragmentTransaction1.commit();
        } else if (id == R.id.nav_discount) {
            drawer.closeDrawer(Gravity.LEFT);
            FragmentManager fragmentManager1 = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction1 = fragmentManager1.beginTransaction();
            fragmentTransaction1.setCustomAnimations(R.anim.fadein, R.anim.fade_out);
            fragmentTransaction1.replace(R.id.container, new DiscountProducts());
            fragmentTransaction1.commit();
        } else if (id == R.id.nav_featured) {
            drawer.closeDrawer(Gravity.LEFT);
            FragmentManager fragmentManager1 = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction1 = fragmentManager1.beginTransaction();
            fragmentTransaction1.setCustomAnimations(R.anim.fadein, R.anim.fade_out);
            fragmentTransaction1.replace(R.id.container, new FeatureProduct());
            fragmentTransaction1.commit();
        } else if (id == R.id.nav_trending) {
            drawer.closeDrawer(Gravity.LEFT);
            FragmentManager fragmentManager1 = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction1 = fragmentManager1.beginTransaction();
            fragmentTransaction1.setCustomAnimations(R.anim.fadein, R.anim.fade_out);
            fragmentTransaction1.replace(R.id.container, new TrendingProducts());
            fragmentTransaction1.commit();
        }else if (id == R.id.nav_Orders) {
            if(sd.getLOGIN_STATUS().equals("skip")){
                showdialog();
            }else{
                drawer.closeDrawer(Gravity.LEFT);
                FragmentManager fragmentManagerpro = getSupportFragmentManager();
                FragmentTransaction fragmentTransactionpro = fragmentManagerpro.beginTransaction();
                fragmentTransactionpro.setCustomAnimations(R.anim.fadein, R.anim.fade_out);
                fragmentTransactionpro.replace(R.id.container, new MyOrdersfargment());
                fragmentTransactionpro.commit();
            }
        }else if (id == R.id.nav_profile) {
            if(sd.getLOGIN_STATUS().equals("skip")){
                  showdialog();
            }else{
                drawer.closeDrawer(Gravity.LEFT);
                UserProfileFragment fragment = new UserProfileFragment();
                Bundle bundle = new Bundle();
                FragmentManager fragmentManager1 = getSupportFragmentManager();
                FragmentTransaction fragmentTransaction1 = fragmentManager1.beginTransaction();
                bundle.putString("order","yes");
                fragment.setArguments(bundle);
                fragmentTransaction1.setCustomAnimations(R.anim.fadein, R.anim.fade_out);
                fragmentTransaction1.replace(R.id.container, fragment);
                fragmentTransaction1.addToBackStack(null);
                fragmentTransaction1.commit();
            }
        } else if (id == R.id.nav_wholasale) {
            drawer.closeDrawer(Gravity.LEFT);
            FragmentManager fragmentManager1 = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction1 = fragmentManager1.beginTransaction();
            fragmentTransaction1.setCustomAnimations(R.anim.fadein, R.anim.fade_out);
            fragmentTransaction1.replace(R.id.container, new WholeSaleFragment());
            fragmentTransaction1.commit();
        } else if (id == R.id.nav_ourallstore) {
            drawer.closeDrawer(Gravity.LEFT);
            FragmentManager fragmentManager1 = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction1 = fragmentManager1.beginTransaction();
            fragmentTransaction1.setCustomAnimations(R.anim.fadein, R.anim.fade_out);
            fragmentTransaction1.replace(R.id.container, new AllStoreFragment());
            fragmentTransaction1.commit();
        } else if (id == R.id.nav_coupan) {
            drawer.closeDrawer(Gravity.LEFT);
            FragmentManager fragmentManager1 = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction1 = fragmentManager1.beginTransaction();
            fragmentTransaction1.setCustomAnimations(R.anim.fadein, R.anim.fade_out);
            fragmentTransaction1.replace(R.id.container, new CoupanFragment());
            fragmentTransaction1.commit();
        } else if (id == R.id.nav_aboutus) {
            drawer.closeDrawer(Gravity.LEFT);
            FragmentManager fragmentManager1 = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction1 = fragmentManager1.beginTransaction();
            fragmentTransaction1.setCustomAnimations(R.anim.fadein, R.anim.fade_out);
            fragmentTransaction1.replace(R.id.container, new AboutUs_Fragment());
            fragmentTransaction1.commit();
        } else if (id == R.id.nav_privacy) {
            drawer.closeDrawer(Gravity.LEFT);
            FragmentManager fragmentManager1 = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction1 = fragmentManager1.beginTransaction();
            fragmentTransaction1.setCustomAnimations(R.anim.fadein, R.anim.fade_out);
            fragmentTransaction1.replace(R.id.container, new PrivacyPolicy_fragment());
            fragmentTransaction1.commit();
        } else if (id == R.id.nav_Terms) {
            drawer.closeDrawer(Gravity.LEFT);
            FragmentManager fragmentManager1 = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction1 = fragmentManager1.beginTransaction();
            fragmentTransaction1.setCustomAnimations(R.anim.fadein, R.anim.fade_out);
            fragmentTransaction1.replace(R.id.container, new TermsAndCondition_fragment());
            fragmentTransaction1.commit();
        } else if (id == R.id.nav_faq) {
            drawer.closeDrawer(Gravity.LEFT);
            FragmentManager fragmentManager1 = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction1 = fragmentManager1.beginTransaction();
            fragmentTransaction1.setCustomAnimations(R.anim.fadein, R.anim.fade_out);
            fragmentTransaction1.replace(R.id.container, new Faq_Fragment());
            fragmentTransaction1.commit();
        } else if (id == R.id.nav_contactus) {
            drawer.closeDrawer(Gravity.LEFT);
            FragmentManager fragmentManager1 = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction1 = fragmentManager1.beginTransaction();
            fragmentTransaction1.setCustomAnimations(R.anim.fadein, R.anim.fade_out);
            fragmentTransaction1.replace(R.id.container, new ContactUsFragment());
            fragmentTransaction1.commit();
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.imgUser:
                Intent in = new Intent(DrawerActivity.this, EditProfile.class);
                in.putExtra("name",name);
                in.putExtra("email",email);
                in.putExtra("mobile",mobile);
                in.putExtra("address",address);
                in.putExtra("city",city);
                in.putExtra("zip",zip);
                in.putExtra("state",state);
                in.putExtra("country",country);
                in.putExtra("photo",photo);
                in.putExtra("building",building);
                in.putExtra("flat_no",flat_no);
                in.putExtra("type","activity");
                startActivityForResult(in, PROFILE_UPDATE_REQUEST);
                overridePendingTransition(R.anim.left_in, R.anim.left_out);
                break;
            case R.id.imgCart:
                FragmentManager fragmentManagercart = getSupportFragmentManager();
                FragmentTransaction fragmentTransactioncart = fragmentManagercart.beginTransaction();
                fragmentTransactioncart.setCustomAnimations(R.anim.fadein, R.anim.fade_out);
                fragmentTransactioncart.replace(R.id.container, new MyCartFragment());
                fragmentTransactioncart.commit();
                break;
            case R.id.imgNotification:
                if(sd.getLOGIN_STATUS().equals("skip")){
                    showdialog();
                }else{
                    FragmentManager fragmentManager1 = getSupportFragmentManager();
                    FragmentTransaction fragmentTransaction1 = fragmentManager1.beginTransaction();
                    fragmentTransaction1.setCustomAnimations(R.anim.fadein, R.anim.fade_out);
                    fragmentTransaction1.replace(R.id.container, new NotificationFragment());
                    fragmentTransaction1.commit();
                }
                break;
            case R.id.imgLogout:
                AlertDialog.Builder builder = new AlertDialog.Builder(DrawerActivity.this);
                ViewGroup viewGroup = findViewById(android.R.id.content);
                View dialogView = LayoutInflater.from(DrawerActivity.this).inflate(R.layout.custom_dialog_layout, viewGroup, false);

                builder.setView(dialogView);
                final AlertDialog alertDialog = builder.create();
                alertDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
                alertDialog.show();

                TextView tvYes , tvNo ,tvheader ;
                tvYes=(TextView)dialogView.findViewById(R.id.tvYes);
                tvNo=(TextView)dialogView.findViewById(R.id.tvNo);
                tvheader=(TextView)dialogView.findViewById(R.id.tvHeader);

                tvheader.setText("Are you sure you want to logout this app ?");
                tvYes.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Toast.makeText(DrawerActivity.this, "Logout", Toast.LENGTH_SHORT).show();
                        sd.setLOGIN_STATUS("false");
                        sd.setLatitude("latitude");
                        sd.setLongitude("longitude");
                        sd.setLocation("location");
                        sd.setKEY_APITOKEN("");
                        Intent i = new Intent(getApplicationContext(), LoginActivity.class);
                        overridePendingTransition(R.anim.left_in, R.anim.left_out);
                        startActivity(i);
                        int pid = android.os.Process.myPid();
                        android.os.Process.killProcess(pid);
                        System.exit(0);
                    }
                });

                tvNo.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        alertDialog.dismiss();
                    }
                });

                break;
            case R.id.ll_Login:
                Intent in1 = new Intent(DrawerActivity.this,LoginActivity.class);
                startActivity(in1);
                finish();
                overridePendingTransition(R.anim.left_in, R.anim.left_out);
                break;
            case R.id.llRegister:
                Intent inr = new Intent(DrawerActivity.this,SignupActivity.class);
                startActivity(inr);
                finish();
                overridePendingTransition(R.anim.left_in, R.anim.left_out);
                break;
            default:

                break;

        }
    }

    public void showdialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(DrawerActivity.this);
        ViewGroup viewGroup = findViewById(android.R.id.content);
        View dialogView = LayoutInflater.from(DrawerActivity.this).inflate(R.layout.login_dialog_layout, viewGroup, false);

        builder.setView(dialogView);
        final AlertDialog alertDialog = builder.create();
        alertDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        alertDialog.show();

        TextView tvlogin , tvSignup ,tvheader ;
        tvlogin=(TextView)dialogView.findViewById(R.id.tvLogin);
        tvSignup=(TextView)dialogView.findViewById(R.id.tvSignup);
        tvheader=(TextView)dialogView.findViewById(R.id.tvHeader);

        tvheader.setText("Sorry , You are not logged in , So Login firstly");

        tvlogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.dismiss();
                Intent in1 = new Intent(DrawerActivity.this,LoginActivity.class);
                startActivity(in1);
                finish();
                overridePendingTransition(R.anim.left_in, R.anim.left_out);

            }
        });

        tvSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.dismiss();
                Intent in1 = new Intent(DrawerActivity.this,SignupActivity.class);
                startActivity(in1);
                finish();
                overridePendingTransition(R.anim.left_in, R.anim.left_out);

            }
        });

    }

    public void retro() {

        try{
            if (!cd.isConnectingToInternet()) {
                Toast.makeText(DrawerActivity.this, "Internet connection not available...", Toast.LENGTH_SHORT).show();
            } else {
                Call<SignupModel> call = apiInterface.getWallet("Bearer "+sd.getKEY_APITOKEN());
                call.enqueue(new Callback<SignupModel>() {
                    @Override
                    public void onResponse(Call<SignupModel> call, retrofit2.Response<SignupModel> response) {

                        SignupModel pro = response.body();
                          if(pro.success.equals("Wallet Found!")){

                            llLogin.setVisibility(View.GONE);
                            llSingup.setVisibility(View.GONE);
                            tvWelcome.setVisibility(View.VISIBLE);

                              name = pro.userdata.get(0).name;
                              mobile = pro.userdata.get(0).phone;
                              email = pro.userdata.get(0).email;
                              zip = pro.userdata.get(0).zip;
                              city = pro.userdata.get(0).city;
                              address = pro.userdata.get(0).address;
                              state = pro.userdata.get(0).state;
                              country = pro.userdata.get(0).country;
                              photo = pro.userdata.get(0).photo;
                              building = pro.userdata.get(0).building;
                              flat_no = pro.userdata.get(0).flat_no;


                            sd.setSETNAME(pro.userdata.get(0).name);
                            sd.setSETEMAIL(pro.userdata.get(0).email);
                            sd.setPHOTO(pro.userdata.get(0).photo);
                            sd.setMobile(pro.userdata.get(0).phone);

                            name = sd.getNAME();
                            email = sd.getEMAIL();
                            photo= sd.getPHOTO();

                            if(!sd.getPHOTO().equals("")){
                                Picasso.with(DrawerActivity.this).load(sd.getPHOTO()).into(circleImageView);
                            }
                            tvName.setText(sd.getNAME());
                            tvEmail.setText(sd.getEMAIL());

                        }else{

                        }

                    }
                    @Override
                    public void onFailure(Call<SignupModel> call, Throwable t) {
                        call.cancel();
                    }
                });
            }
        }catch (Exception e){

        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==PROFILE_UPDATE_REQUEST) {
          retro();
        }

    }
}

