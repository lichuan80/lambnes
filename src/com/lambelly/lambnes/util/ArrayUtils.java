/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.lambelly.lambnes.util;
import org.apache.log4j.*;
import java.util.Arrays;
import java.util.List;
import java.util.Collection;

/**
 *
 * @author thomasmccarthy
 */
public class ArrayUtils
{
    private static Logger logger = Logger.getLogger(ArrayUtils.class);

    /**
     * returns an array of a larger size buffered with the buffer byte
     *
     * @param a
     * @param finalLength
     * @param buffer
     * @return
     */
    public static int[] bufferArray(int[] a, int finalLength, int buffer)
    {
        int[] b = new int[finalLength];

        for (int i=0; i < finalLength; i++)
        {
            if (i < a.length)
            {
                // copy existing nodes
                b[i] = a[i];
            }
            else
            { 
                // add buffer until the end
                b[i] = buffer;
            }
        }

        return b;
    }

    /**
     * sends a portion of the top of the array to logger
     * @param a
     * @param headSize
     */
    public static void head(int[] a, int headSize)
    {
        for (int i=0;i < headSize;i++)
        {
            logger.debug("head " + i + ":" + a[i]);
        }
    }

    /**
     * sends a portion of the bottom of the array to logger
     * @param a
     * @param footSize
     */
    public static void foot(int[] a, int footSize)
    {
        for (int i=(a.length-footSize);i<a.length;i++)
        {
            logger.debug("foot " + i + ":" + a[i]);
        }
    }

    /**
     * print the array to logger
     * @param a
     */
    public static void printArray(int[] a)
    {
        for (int i=0;i<a.length;i++)
        {
            logger.debug("a[" + i + "]: " + a[i]);
        }
    }
}
