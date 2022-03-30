package com.example.cody.liverhub;

import com.example.cody.liverhub.Objects.LiverModel;

import java.util.ArrayList;
import java.util.List;

public class Constant {
    public static final int BYTES_PER_FLOAT = 4;
    public static final int VERT_PER_FLUSH = 600000;
    public static final int VERT_PER_FACET = 3;
    public static final int REQ_LOGIN_STATUES = 10010;
    public static final int REQ_SIGNUP_STATUES = 10014;
    public static final int FACIAL_VAL = 10200;
    public static List<LiverModel> dynamic_model = new ArrayList<>();
}
