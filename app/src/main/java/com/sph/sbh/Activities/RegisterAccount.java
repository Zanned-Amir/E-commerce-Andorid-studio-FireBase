package com.sph.sbh.Activities;

import static android.preference.PreferenceManager.getDefaultSharedPreferences;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.sph.sbh.Model.User;
import com.sph.sbh.R;

import java.util.Calendar;

public class RegisterAccount extends AppCompatActivity {

    private boolean isPasswordVisible = false;
    boolean isFisrtTime;

    private EditText addresse1 ,addresse2 , editTextEmail, passwordEditText, editTextBirthDate, editTextName, editTextLastName, editTextPhoneNumber, editTextConfirmPassword;
    private Spinner spinnerState;
    private RadioGroup radioGroupGender;
    private FirebaseAuth auth;
    private boolean isFirstTime;

    SharedPreferences  sharedPreferences;
    DatabaseReference databaseReference;
    SharedPreferences settings;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_account);

        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
        databaseReference = FirebaseDatabase.getInstance().getReference().child("users");

         auth = FirebaseAuth.getInstance();
        editTextEmail =findViewById(R.id.editTextEmail);
        editTextConfirmPassword = findViewById(R.id.editTextConfirmPassword);
        passwordEditText = findViewById(R.id.editTextPassword);
        editTextBirthDate = findViewById(R.id.editTextBirthDate);
        editTextName = findViewById(R.id.editTextName);
        editTextLastName = findViewById(R.id.editTextLastName);
        editTextPhoneNumber = findViewById(R.id.editTextPhoneNumber);
        spinnerState = findViewById(R.id.spinnerState);
        radioGroupGender = findViewById(R.id.radioGroupGender);
        addresse1 = findViewById(R.id.editTextAddress1);
        addresse2 = findViewById(R.id.editTextAddress2);
        TextInputLayout textInputLayoutPassword = findViewById(R.id.textInputLayoutPassword);


        sharedPreferences = getSharedPreferences("userData",MODE_PRIVATE);




        textInputLayoutPassword.setStartIconOnClickListener(v -> {
            // Toggle password visibility
            isPasswordVisible = !isPasswordVisible;
            togglePasswordVisibility();
        });


        editTextBirthDate.addTextChangedListener(birthDateTextWatcher);


    }
    public void register(View view) {
        // Get the text from all input fields
        String password = passwordEditText.getText().toString().trim();
        String confirmPassword = editTextConfirmPassword.getText().toString().trim();
        String birthDate = editTextBirthDate.getText().toString().trim();
        String name = editTextName.getText().toString().trim();
        String lastName = editTextLastName.getText().toString().trim();
        String phoneNumber = editTextPhoneNumber.getText().toString().trim();
        String email = editTextEmail.getText().toString().trim();
        String selectedState = (String) spinnerState.getSelectedItem();
        String gender = getSelectedGender();

        String addr1 = addresse1.getText().toString().trim();
        String addr2 = addresse2.getText().toString().trim();

        if (gender.isEmpty()) {
            Toast.makeText(RegisterAccount.this, "Choose gender", Toast.LENGTH_SHORT).show();
            return;
        }


        if (!isValidBirthDate(birthDate)) {
            Toast.makeText(RegisterAccount.this, "Invalid birth date", Toast.LENGTH_SHORT).show();
            return;
        } else {
            birthDate = convertToDashesFormat(birthDate);
        }


        if (!password.equals(confirmPassword)) {
            Toast.makeText(RegisterAccount.this, "Passwords do not match", Toast.LENGTH_SHORT).show();
            return;
        }


        if (password.length() < 6) {
            Toast.makeText(RegisterAccount.this, "Password should be at least 6 characters long", Toast.LENGTH_SHORT).show();
            return;
        }


        if (name.isEmpty()) {
            Toast.makeText(RegisterAccount.this, "Enter your name", Toast.LENGTH_SHORT).show();
            return;
        }

        if (lastName.isEmpty()) {
            Toast.makeText(RegisterAccount.this, "Enter your last name", Toast.LENGTH_SHORT).show();
            return;
        }


        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            Toast.makeText(RegisterAccount.this, "Enter a valid email address", Toast.LENGTH_SHORT).show();
            return;
        }


        if (phoneNumber.isEmpty() || !android.util.Patterns.PHONE.matcher(phoneNumber).matches()) {
            Toast.makeText(RegisterAccount.this, "Enter a valid phone number", Toast.LENGTH_SHORT).show();
            return;
        }

        if (selectedState.isEmpty() || selectedState.equals("Select State")) {
            Toast.makeText(RegisterAccount.this, "Select your state", Toast.LENGTH_SHORT).show();
            return;
        }

        User user = new User(email, name, lastName, phoneNumber, selectedState, birthDate, gender,addr1, addr2 );


        String finalBirthDate = birthDate;
        auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(RegisterAccount.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {

                            Toast.makeText(RegisterAccount.this, "Registration successful", Toast.LENGTH_SHORT).show();

                            String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();

                            databaseReference.child(uid).setValue(user);

                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.putString("email", email);
                            editor.putString("name", name);
                            editor.putString("lastName", lastName);
                            editor.putString("phoneNumber", phoneNumber);
                            editor.putString("state", selectedState);
                            editor.putString("birthDate", finalBirthDate);
                            editor.putString("gender", gender);
                            editor.putString("addr1", addr1);
                            editor.putString("addr2", addr2);
                            editor.apply();



                            Toast.makeText(RegisterAccount.this, "Data inserted", Toast.LENGTH_SHORT).show();


                            Intent intent = new Intent(RegisterAccount.this, Boarding.class);

                            startActivity(intent);
                            finish();
                        } else {

                            Toast.makeText(RegisterAccount.this, "Registration failed: " + task.getException(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }


    private String convertToDashesFormat(String birthDate) {
        // Split the birth date string into day, month, and year
        String[] parts = birthDate.split("/");
        if (parts.length != 3) {
            // Invalid format
            return null;
        }
        int day, month, year;
        try {
            day = Integer.parseInt(parts[0]);
            month = Integer.parseInt(parts[1]);
            year = Integer.parseInt(parts[2]);
        } catch (NumberFormatException e) {
            // Non-numeric parts
            return null;
        }

        // Format the date with dashes
        return String.format("%02d-%02d-%04d", day, month, year);
    }



    private boolean isValidBirthDate(String birthDate) {
        // Parse the birth date string into day, month, and year
        String[] parts = birthDate.split("/");
        if (parts.length != 3) {
            // Invalid format
            return false;
        }
        int day, month, year;
        try {
            day = Integer.parseInt(parts[0]);
            month = Integer.parseInt(parts[1]);
            year = Integer.parseInt(parts[2]);
        } catch (NumberFormatException e) {
            // Non-numeric parts
            return false;
        }

        // Validate month
        if (month < 1 || month > 12) {
            return false;
        }

        // Validate day based on month and year
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.YEAR, year);
        cal.set(Calendar.MONTH, month - 1); // Month is zero-based in Calendar
        int maxDay = cal.getActualMaximum(Calendar.DAY_OF_MONTH);
        if (day < 1 || day > maxDay) {
            return false;
        }

        // Limit year
        int currentYear = Calendar.getInstance().get(Calendar.YEAR);
        if (year < 1900 || year > currentYear) {
            return false;
        }

        // Date is valid
        return true;
    }


    // Method to get the selected gender from RadioGroup
    private String getSelectedGender() {
        int selectedId = radioGroupGender.getCheckedRadioButtonId();
        if (selectedId == R.id.radioButtonMale) {
            return "Male";
        } else if (selectedId == R.id.radioButtonFemale) {
            return "Female";
        }
        return "";
    }


    // TextWatcher to format birth date input as "dd/mm/yyyy" and validate day, month, and year
    private final TextWatcher birthDateTextWatcher = new TextWatcher() {
        private String current = "";
        private String ddmmyyyy = "DDMMYYYY";
        private Calendar cal = Calendar.getInstance();
        private int currentYear = cal.get(Calendar.YEAR);

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {}

        @Override
        public void afterTextChanged(Editable s) {
            if (!s.toString().equals(current)) {
                String clean = s.toString().replaceAll("[^\\d.]", "");
                String cleanC = current.replaceAll("[^\\d.]", "");

                int cl = clean.length();
                int sel = cl;
                for (int i = 2; i <= cl && i < 6; i += 2) {
                    sel++;
                }

                if (clean.equals(cleanC)) sel--;

                if (clean.length() < 8){
                    clean = clean + ddmmyyyy.substring(clean.length());
                } else {
                    int day  = Integer.parseInt(clean.substring(0, 2));
                    int month  = Integer.parseInt(clean.substring(2, 4));
                    int year = Integer.parseInt(clean.substring(4, 8));

                    // Validate month
                    if (month < 1 || month > 12) {
                        Toast.makeText(RegisterAccount.this, "Invalid month", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    // Validate day based on month and year
                    cal.set(year, month - 1, 1); // Set calendar to the first day of the given month and year
                    int maxDay = cal.getActualMaximum(Calendar.DAY_OF_MONTH);
                    if (day < 1 || day > maxDay) {
                        Toast.makeText(RegisterAccount.this, "Invalid day", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    // Validate year
                    if (clean.substring(4, 8).length() < 4) {
                        Toast.makeText(RegisterAccount.this, "Invalid year", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    // Limit year
                    if (year < 1900 || year > currentYear) {
                        Toast.makeText(RegisterAccount.this, "Year must be between 1900 and " + currentYear, Toast.LENGTH_SHORT).show();
                        return;
                    }

                    clean = String.format("%02d%02d%04d", day, month, year);
                }

                clean = String.format("%s/%s/%s", clean.substring(0, 2),
                        clean.substring(2, 4),
                        clean.substring(4, 8));

                sel = sel < 0 ? 0 : sel;
                current = clean;
                editTextBirthDate.setText(current);
                editTextBirthDate.setSelection(sel < current.length() ? sel : current.length());
            }
        }

    };

    private void togglePasswordVisibility() {
        if (isPasswordVisible) {
            // Show password
            passwordEditText.setTransformationMethod(null);
        } else {
            // Hide password
            passwordEditText.setTransformationMethod(new android.text.method.PasswordTransformationMethod());
        }

        // Move cursor to the end of the text
        passwordEditText.setSelection(passwordEditText.getText().length());
    }

    public void login(View view) {
        startActivity(new Intent(RegisterAccount.this, LoginAccount.class));
    }


}




