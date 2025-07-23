# Glyph-Utils
This Java class provides an easy way to render frame-based animations to the Glyph Interface. It also has some useful methods that I used while developing my App Dot Hub. Please note that it just makes developing easier. You still need some basic understanding of Java to use it.

## Before you Start
Please read the documentation of the GlyphMatrix-Developer-Kit and import it into your project. To create complex animations, I recommend using Blender, but you can also draw your frames by hand. If you don't know how to start, you can look at my Example Glyph toy in this Repository. More on that at the End.


## Methods

| Return type                    | Method                         | Description                    |
|:-------------------------------|:-------------------------------|:-------------------------------|
| void           | `renderAnimation(GlyphMatrixManager mGM, String resNameWithoutNumber, int numberOfFrames, int mode, Context context, int ANIMATION_INTERVAL, ValueAnimator animation)`       | Prepares your Animation. Use `exampleanimator.start()` to start the animation.         |
| void           | `renderFrameToGlyphMatrix(GlyphMatrixManager mGM, GlyphMatrixFrame frame)`                      | Renders a GlyphMatrixFrame to the Glyph Matrix.          |
| void           | `renderImageToGlyphMatrix(GlyphMatrixManager mGM, int drawableId, Context context)`       | Renders an Image to the Glyph Matrix.|
| GlyphMatrixFrame[]           | `setAllAnimationFramesAsArray(Context context, String frameResName, int numberOfFrames, int mode)`   | Creates an Array of GlyphMatrixFrames with your animation Frames. The foreground is your frame. The amount of Frames is determined by numberOfFrames. If your frames are named frame1, frame2, frame3... You have to use mode 1. If your frames are named frame0001, frame0002, frame0003... You have to use mode 2. |
| GlyphMatrixObject           | `createObjectFromResourceName(Context context, String resName)` | Returns an GLyphMatrix object with your resource.                  |
| GlyphMatrixFrame           | `createFrameOutOfDrawable(Context context, String foregroundResName)`   | Returns an GLyphMatrixFrame with your resource. |
| Bitmap           | `drawableToBitmap(Context context, int drawableId)` | Returns an Bitmap from your drawable                 |

There are two ways a Method needs your drawable. Most Methods want your drawable as an ID. You can just give the Method `R.drawable.yourDrawable`. The other way is with the Name. Most programs like Blender create the Frames numbered starting with either 0001 or 1. If your frames are named frame1, frame2, frame3... You have to put 1 for mode. If your frames are named frame0001, frame0002, frame0003... You have to use mode 2. 

I recommend, that your Glyph Toy Class implements an `Animator.AnimatorListener`, and it also needs to extend `android.app.Service`.

## Example Glyph Toys
I have provided a simple Coin Flip Glyph Toy, that when selected in the Glyph Matrix shows an Image of a coin. Longpressing starts the animation. when selected in the Glyph Matrix, shows an image of a coin. Long-pressing starts the animation. When the animation finishes, it shows either the head or tails.
