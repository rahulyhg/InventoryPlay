/*
 * Copyright (C) 2017 MINDORKS NEXTGEN PRIVATE LIMITED
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://mindorks.com/license/apache-v2
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License
 */

package com.dell.inventoryplay;

/**
 * Created by Sasikanta Sahoo on 08/01/17.
 * AppConfig
 */

public class AppConfig {

    public static final String API_BASE_URL = "https://dit.apidp.dell.com/inventory/api";
    public static final String DATAPOWER_API_KEY = "?apikey=87729a58-aadb-45fd-989f-cb235d1493e7";
    public static final String REST_API_HEALTH_CHECK = API_BASE_URL+"/HealthCheck"+DATAPOWER_API_KEY;
    public static final String REST_API_LOGIN = API_BASE_URL+"/ValidateUser"+DATAPOWER_API_KEY;
    public static final String REST_API_CHECK_POINT_TITLE = API_BASE_URL+"/CheckPointHealth/GetTitle"+DATAPOWER_API_KEY;
    public static final String REST_API_CHECK_POINT_HEALTH = API_BASE_URL+"/CheckPointHealth/GetAlertData"+DATAPOWER_API_KEY;
    public static final String REST_API_ASN_APP = "http://www.mocky.io/";
    public static final String REST_API_TRACK_ASN_APP_ERROR = "http://www.mocky.io/";
    public static Boolean useMockJson= true;
}