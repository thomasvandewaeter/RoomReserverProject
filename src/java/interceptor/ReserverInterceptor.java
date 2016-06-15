/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package interceptor;

import java.io.Serializable;
import javax.interceptor.AroundInvoke;
import javax.interceptor.Interceptor;
import javax.interceptor.InvocationContext;
import javax.transaction.Transactional;

/**
 *
 * @author Thomas
 */
@Interceptor
@Transactional
public class ReserverInterceptor implements Serializable{
    
    @AroundInvoke
    public Object MethodEntry(InvocationContext invocationContext) throws Exception{
        System.out.println("@Interceptor - Entering method: " + invocationContext.getMethod().getName());
        
        return invocationContext.proceed();
    }
}
