package com.source.utang.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.source.utang.R;

import static android.content.ContentValues.TAG;

/**
 * Created by chandra on 04/01/2018.
 */

public class AddUser extends Fragment {
    private Button btSignUp;
    private Button btSignIn;
    private Button btSignOut;

    private EditText etEmail;
    private EditText etPassword;

    private FirebaseAuth fAuth;
    private FirebaseAuth.AuthStateListener fStateListener;

    //private static final String TAG = getSimpleName();

    public AddUser(){

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        fAuth = FirebaseAuth.getInstance();
    }
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.form_user, container, false);

        fStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    // User sedang login
                    Log.v(TAG, "onAuthStateChanged:signed_in:" + user.getUid());
                } else {
                    /**
                     * Method ini akan dipanggil apabila user berhasil logout
                     */
                    Toast.makeText(getContext(), "User Logout\n",
                            Toast.LENGTH_SHORT).show();
                    btSignOut.setEnabled(false);
                    Log.v(TAG, "onAuthStateChanged:signed_out");
                }
            }

        };
        btSignUp = (Button) view.findViewById(R.id.bt_signup);
        btSignIn = (Button) view.findViewById(R.id.bt_signin);
        btSignOut = (Button) view.findViewById(R.id.bt_signout);

        etEmail = (EditText) view.findViewById(R.id.et_email);
        etPassword = (EditText) view.findViewById(R.id.et_password);

        btSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /**
                 * Lempar email dan password ketika tombol signup diklik
                 */
                signUp(etEmail.getText().toString(), etPassword.getText().toString());
            }
        });

        btSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /**
                 * Lempar email dan password ketika tombol signin diklik
                 */
                signIn(etEmail.getText().toString(), etPassword.getText().toString());
            }
        });

        btSignOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signOut();
            }
        });

        checkLogin();
        return view;
    }

    private void signUp(final String email, String password){
        fAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d(TAG, "createUserWithEmail:onComplete:" + task.isSuccessful());

                        /**
                         * Jika sign in gagal, tampilkan pesan ke user. Jika sign in sukses
                         * maka auth state listener akan dipanggil dan logic untuk menghandle
                         * signed in user bisa dihandle di listener.
                         */
                        if (!task.isSuccessful()) {
                            task.getException().printStackTrace();
                            Toast.makeText(getActivity(), "Proses Pendaftaran Gagal",
                                    Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(getActivity(), "Proses Pendaftaran Berhasil\n" +
                                            "Email "+email,
                                    Toast.LENGTH_SHORT).show();
                        }

                        // rest of code
                    }
                });

    }

    private void signIn(final String email, String password){
        fAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d(TAG, "signInWithEmail:onComplete:" + task.isSuccessful());

                        /**
                         * Jika sign in gagal, tampilkan pesan ke user. Jika sign in sukses
                         * maka auth state listener akan dipanggil dan logic untuk menghandle
                         * signed in user bisa dihandle di listener.
                         */
                        if (!task.isSuccessful()) {
                            Log.w(TAG, "signInWithEmail:failed", task.getException());
                            Toast.makeText(getActivity(), "Proses Login Gagal\n",
                                    Toast.LENGTH_SHORT).show();
                        } else{

                            /**
                             * set enabled pada button logout apabila user berhasil login
                             */
                            btSignOut.setEnabled(true);
                            Toast.makeText(getActivity(), "Login Berhasil\n" +
                                            "Email "+email,
                                    Toast.LENGTH_SHORT).show();
                        }
                        // ... rest of code
                    }
                });
    }

    private void signOut(){

        /**
         * Method untuk sign out dari Firebase
         */
        fAuth.signOut();
    }

    private void checkLogin(){
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            // Nama email address photo uri
            String email = user.getEmail();

            // The user's ID, unique to the Firebase project. Do NOT use this value to
            // authenticate with your backend server, if you have one. Use
            // FirebaseUser.getToken() instead.
            String uid = user.getUid();

            Toast.makeText(getActivity(), "Login sebagai\n" + email+" "+uid,
                    Toast.LENGTH_SHORT).show();
            btSignOut.setEnabled(true);
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        fAuth.addAuthStateListener(fStateListener);
    }

    @Override
    public void onResume() {
        super.onResume();
        fAuth.addAuthStateListener(fStateListener);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (fStateListener != null) {
            fAuth.removeAuthStateListener(fStateListener);
        }
    }
}
