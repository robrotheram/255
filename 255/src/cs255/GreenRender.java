/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cs255;

import java.awt.Color;
import java.awt.Paint;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.chart.renderer.xy.XYBarRenderer;

/**
 *
 * @author Robert
 */
public class GreenRender extends XYBarRenderer
{

   public GreenRender()
   {
   }

   public Paint getItemPaint(final int row, final int column)
   {
      // returns color depending on y coordinate.
      return Color.GREEN;
   }

}
