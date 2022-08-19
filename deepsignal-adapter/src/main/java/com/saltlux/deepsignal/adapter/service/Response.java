package com.saltlux.deepsignal.adapter.service;

import java.io.PrintWriter;
import java.io.StringWriter;

public final class Response {

    /*
     * request_id!=null인 경우 HTTPClient를 사용하여 접속한 경우,
     * javascript나 다른 http client를 사용하여 요청한 경우는 지정할 필요가 없으므로 request_id=null이다.
     */
    final String request_id; // 요청자 식별 ID(=>응답 결과로 돌려준다)

    final int result; //0은 성공, 그외 값을 실패
    final String reason; //실패 이유

    /**
     * @deprecated API 호환성을 위해서 사용하고, 향후에는 제거하자
     */
    final String return_type;
    final Object return_object;

    final com.google.gson.Gson m_gson;

    Response(final String request_id, final int result, final String reason, final Object returnObject) {
        this.request_id = request_id;

        this.result = result;
        this.reason = reason;

        //제공한 API(HTTP Client)를 사용하여 요청한 경우만 return_type을 전달하고, HTTP로 직접 호출한 경우에는 노출하지 안는다.
        this.return_type = (returnObject != null && request_id != null) ? returnObject.getClass().getName() : null;

        this.return_object = returnObject;

        this.m_gson = null;
    }

    Response(
        final String request_id,
        final int result,
        final String reason,
        final String returnType,
        final com.google.gson.JsonElement returnObj,
        final com.google.gson.Gson gson
    ) {
        this.request_id = request_id;

        this.result = result;
        this.reason = reason;

        this.return_type = returnType;
        this.return_object = returnObj;
        this.m_gson = gson;
    }

    public boolean succeeded() {
        return (result == 0);
    }

    public int errorCode() {
        return result;
    }

    public String errorReason() {
        return reason;
    }

    /*public <T> T getReturn() 	{ //사용하지 못하게 하자
		try {
			return (T) m_gson.fromJson(return_object, Class.forName(this.return_type));
		} catch (JsonSyntaxException | ClassNotFoundException e) {
			return null;
		}
	}*/

    public <T> T getReturn(Class<T> classOfT) {
        return getReturn(this.m_gson, classOfT);
    }

    @SuppressWarnings("unchecked")
    public <T> T getReturn(com.google.gson.Gson gson, Class<T> classOfT) {
        if (return_object == null) return null;
        if (return_object instanceof com.google.gson.JsonElement) return gson.fromJson(
            (com.google.gson.JsonElement) return_object,
            classOfT
        );
        return (T) return_object;
    }

    public boolean isResult(Response r) {
        return (this.result == r.result);
    }

    //////////////////////////////////////////////////////////////////////////////////
    public static Response onSuccess(final String request_id, final Object returnObj) {
        return new Response(request_id, 0, null, returnObj);
    }

    public static Response onFailure(final String request_id, final int result, final String resultReson) {
        return new Response(request_id, result, resultReson, null);
    }

    public static Response onFailure(final String request_id, final String resultReson) {
        return new Response(request_id, -1, resultReson, null);
    }

    public static Response onFailure(final String request_id, final Throwable e) {
        StringWriter sw = new StringWriter();
        e.printStackTrace(new PrintWriter(sw));
        return new Response(request_id, -1, sw.toString(), null);
    }

    //////////////////////////////////////////////////////////////////////////////////
    protected static final Response NO_SUPPORT_CONTENT_TYPE;
    protected static final Response TIME_OUT;
    protected static final Response INTERRUPTED;

    public static final Response NOT_SUPPORTED;
    public static final Response NOT_IMPLEMENTED;

    public static final Response CONNECTION_REFUSED;
    public static final Response CONNECTION_RESET;

    public static final Response INVALID_PARAMETER;
    public static final Response INVALID_ARGUMENT;
    public static final Response INVALID_FORMAT;

    static {
        //주의) 여기있는 코드와 문자열은 NRPC 소스에 정의되어 있으니, 동일한 값으로 맞추어 주자
        NO_SUPPORT_CONTENT_TYPE = Response.onFailure(null, 200, "No support content type!");

        TIME_OUT = Response.onFailure(null, 10, "Timeout");
        INTERRUPTED = Response.onFailure(null, 11, "InterruptedException");

        NOT_SUPPORTED = Response.onFailure(null, 70, "No such function supported");
        NOT_IMPLEMENTED = Response.onFailure(null, 71, "Not Implemented yet"); //구현되지 않는 기능을 요청함(업그레이드 버전에서 제공 계정)

        CONNECTION_REFUSED = Response.onFailure(null, 101, "Connection refused");
        CONNECTION_RESET = Response.onFailure(null, 102, "Connection reset by peer");

        //Parameter가 잘못된 경우: 변수 형식, 갯수 등=>boolean FunX(int a, String b)의 경우 "int a"를 Parameter라고 부름
        INVALID_PARAMETER = Response.onFailure(null, 201, "One or more parameters are not valid");

        //Argument가 잘못된 경우 : 변수 값 자체: =>FunX(10, "string")의 경우 '10'과 'string'을 Argument라고 부름
        INVALID_ARGUMENT = Response.onFailure(null, 202, "One or more arguments are not valid");

        //Argument가 잘못된 경우 : 변수 값 자체: =>FunX(10, "string")의 경우 '10'과 'string'을 Argument라고 부름
        INVALID_FORMAT = Response.onFailure(null, 210, "One or more arguments are not valid");
    }
}
