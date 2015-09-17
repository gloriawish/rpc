package com.alibaba.middleware.race.rpc.api;

import com.alibaba.middleware.race.rpc.aop.ConsumerHook;
import com.alibaba.middleware.race.rpc.async.ResponseCallbackListener;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * Created by huangsheng.hs on 2015/3/26.
 */
public class RpcConsumer implements InvocationHandler {
    private Class<?> interfaceClazz;
    public RpcConsumer() {
    }

    /**
     * init a rpc consumer
     */
    private void init() {
        //TODO
    }

    /**
     * set the interface which this consumer want to use
     * actually,it will call a remote service to get the result of this interface's methods
     *
     * @param interfaceClass
     * @return
     */
    public RpcConsumer interfaceClass(Class<?> interfaceClass) {
        this.interfaceClazz = interfaceClass;
        return this;
    }

    ;

    /**
     * set the version of the service
     *
     * @param version
     * @return
     */
    public RpcConsumer version(String version) {
        //TODO
        return this;
    }

    /**
     * set the timeout of the service
     * consumer's time will take precedence of the provider's timeout
     *
     * @param clientTimeout
     * @return
     */
    public RpcConsumer clientTimeout(int clientTimeout) {
        //TODO
        return this;
    }

    /**
     * register a consumer hook to this service
     * @param hook
     * @return
     */
    public RpcConsumer hook(ConsumerHook hook) {
        return this;
    }

    /**
     * return an Object which can cast to the interface class
     *
     * @return
     */
    public Object instance() {
        //TODO return an Proxy
        return Proxy.newProxyInstance(this.getClass().getClassLoader(),new Class[]{this.interfaceClazz},this);
    }

    /**
     * mark a async method,default future call
     *
     * @param methodName
     */
    public void asynCall(String methodName) {
        asynCall(methodName, null);
    }

    /**
     * mark a async method with a callback listener
     *
     * @param methodName
     * @param callbackListener
     */
    public <T extends ResponseCallbackListener> void asynCall(String methodName, T callbackListener) {
        //TODO
    }

    public void cancelAsyn(String methodName) {
        //TODO
    }


    /**
     * Processes a method invocation on a proxy instance and returns
     * the result.  This method will be invoked on an invocation handler
     * when a method is invoked on a proxy instance that it is
     * associated with.
     *
     * @param proxy  the proxy instance that the method was invoked on
     * @param method the {@code Method} instance corresponding to
     *               the interface method invoked on the proxy instance.  The declaring
     *               class of the {@code Method} object will be the interface that
     *               the method was declared in, which may be a superinterface of the
     *               proxy interface that the proxy class inherits the method through.
     * @param args   an array of objects containing the values of the
     *               arguments passed in the method invocation on the proxy instance,
     *               or {@code null} if interface method takes no arguments.
     *               Arguments of primitive types are wrapped in instances of the
     *               appropriate primitive wrapper class, such as
     *               {@code java.lang.Integer} or {@code java.lang.Boolean}.
     * @return the value to return from the method invocation on the
     * proxy instance.  If the declared return type of the interface
     * method is a primitive type, then the value returned by
     * this method must be an instance of the corresponding primitive
     * wrapper class; otherwise, it must be a type assignable to the
     * declared return type.  If the value returned by this method is
     * {@code null} and the interface method's return type is
     * primitive, then a {@code NullPointerException} will be
     * thrown by the method invocation on the proxy instance.  If the
     * value returned by this method is otherwise not compatible with
     * the interface method's declared return type as described above,
     * a {@code ClassCastException} will be thrown by the method
     * invocation on the proxy instance.
     * @throws Throwable the exception to throw from the method
     *                   invocation on the proxy instance.  The exception's type must be
     *                   assignable either to any of the exception types declared in the
     *                   {@code throws} clause of the interface method or to the
     *                   unchecked exception types {@code java.lang.RuntimeException}
     *                   or {@code java.lang.Error}.  If a checked exception is
     *                   thrown by this method that is not assignable to any of the
     *                   exception types declared in the {@code throws} clause of
     *                   the interface method, then an
     *                   {@link UndeclaredThrowableException} containing the
     *                   exception that was thrown by this method will be thrown by the
     *                   method invocation on the proxy instance.
     * @see UndeclaredThrowableException
     */
    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        return null;
    }
}

