package com.rishabh.concurrent.aspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class ExampleAspect {

    Logger LOGGER = LoggerFactory.getLogger(ExampleAspect.class);

    @Around("@annotation(LogExecutionTime)")
    public Object PrintToConsole(ProceedingJoinPoint joinPoint) throws Throwable {
        //return joinPoint.proceed();

        /** Start the timer */
        long start = System.currentTimeMillis();

        /** Continue executing the method */
        Object proceed = joinPoint.proceed();

        /** After method execution is finished the control would return back to this Aspect,
         * So get the current timestamp to get the execution time of method.
         * */
        long executionTime = System.currentTimeMillis() - start;

        //System.out.println(joinPoint.getSignature() + " executed in " + executionTime + "ms");
        LOGGER.info("Time Taken to Execute process in method [{}] executed in {} ms",joinPoint.getSignature().getName() ,executionTime);

        return proceed;
    }
}
