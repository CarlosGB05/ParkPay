package com.example.parkpay;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import dao.UsuarioDAO;
import models.Usuario;

public class Info_Usuario extends AppCompatActivity {

    private TextView info_name,info_dni,info_email,info_phone;
    private Usuario usuario;
    private UsuarioDAO dao;
    private ImageView imagenIcono;
    private AlertDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_info_usuario);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        this.usuario = (Usuario) getIntent().getSerializableExtra("usuario");

        this.imagenIcono = findViewById(R.id.id_img_icon);
        cargarIcono();

        this.info_name = findViewById(R.id.id_text_user_infoName);
        this.info_name.setText(this.usuario.getNombreCompleto().toString());
        this.info_phone = findViewById(R.id.id_text_user_infoPhone);
        this.info_phone.setText(String.valueOf(this.usuario.getTelefono()));
        this.info_dni = findViewById(R.id.id_text_user_infoDni);
        this.info_dni.setText(this.usuario.getDni().toString());
        this.info_email = findViewById(R.id.id_text_user_infoEmail);
        this.info_email.setText(this.usuario.getEmail().toString());

        this.imagenIcono.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                View dialogView = getLayoutInflater().inflate(R.layout.dialog_icon, null);

                AlertDialog.Builder builder = new AlertDialog.Builder(Info_Usuario.this);
                builder.setView(dialogView);

                builder.setCancelable(false);

                Info_Usuario.this.dialog = builder.create();
                dialog.show();

                if (dialog.getWindow() != null) {
                    dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

                    dialog.getWindow().setLayout(
                            WindowManager.LayoutParams.WRAP_CONTENT,
                            WindowManager.LayoutParams.WRAP_CONTENT
                    );

                    dialog.getWindow().setGravity(Gravity.CENTER);
                }

                ImageView iconoAzul = dialogView.findViewById(R.id.id_img_icon_azul);
                ImageView iconoRosa = dialogView.findViewById(R.id.id_img_icon_rosa);

                iconoAzul.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v2) {
                        UsuarioDAO dao = new UsuarioDAO();
                        if (dao.actualizarIcono(Info_Usuario.this.usuario.getIdUsuario(),"icono_azul")) {
                            Info_Usuario.this.imagenIcono.setImageResource(R.mipmap.icono_azul);
                            dao.cerrarConexion();
                            dialog.dismiss();
                        }
                    }
                });

                iconoRosa.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v2) {
                        UsuarioDAO dao = new UsuarioDAO();
                        if (dao.actualizarIcono(Info_Usuario.this.usuario.getIdUsuario(),"icono_rosa")) {
                            Info_Usuario.this.imagenIcono.setImageResource(R.mipmap.icono_rosa);
                            dao.cerrarConexion();
                            dialog.dismiss();
                        }
                    }
                });

                Button btn = dialogView.findViewById(R.id.id_bt_accept_icon);
                btn.setOnClickListener(vc -> {

                    dialog.dismiss();
                });
            }
        });

    }

    public void cargarIcono() {
        this.dao = new UsuarioDAO();
        String icono = this.dao.buscarIcono(this.usuario.getIdUsuario());
        switch (icono) {
            case "icono_azul":
                this.imagenIcono.setImageResource(R.mipmap.icono_azul);
                this.dao.cerrarConexion();
                break;
            case "icono_rosa":
                this.imagenIcono.setImageResource(R.mipmap.icono_rosa);
                this.dao.cerrarConexion();
                break;
            default:
                this.imagenIcono.setImageResource(R.mipmap.icono_default);
                this.dao.cerrarConexion();
                break;
        }
    }

    public void editarUsuario(View view) {
        Intent intent = new Intent(this, Editar_Usuario.class);
        intent.putExtra("usuario",usuario);
        startActivity(intent);
    }

    public void volverMenu(View view) {
        Intent intent = new Intent(this, Menu_Inicial.class);
        intent.putExtra("usuario",usuario);
        startActivity(intent);
    }

}