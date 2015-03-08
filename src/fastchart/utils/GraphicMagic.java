package fastchart.utils;

import java.io.File;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.List;

import org.lwjgl.BufferUtils;
import org.lwjgl.Sys;
import org.lwjgl.glfw.Callbacks;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.glfw.GLFWKeyCallback;
import org.lwjgl.glfw.GLFWvidmode;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GLContext;
import org.lwjgl.system.MemoryUtil;

public class GraphicMagic implements Runnable{

	// We need to strongly reference callback instances.
	private GLFWErrorCallback errorCallback;
	private GLFWKeyCallback keyCallback;

	// The window handle
	private long window;
	
	List<Panel> panels = new ArrayList<>();
	private String title;
	private int height;
	private int width;

	static {

		String architecture = System.getProperty("os.arch");

		if (architecture.contains("64")) {
			architecture = "x64";
		} else if (architecture.contains("32")) {
			architecture = "x32";
		} else {
			System.err.println("Unknown architecture");
		}

		System.out.println(architecture);

		if (System.getProperty("os.name").contains("Windows")) {
			// Windows
			System.setProperty("org.lwjgl.librarypath", new File("lwjgl/native/windows/" + architecture).getAbsolutePath());
		} else if (System.getProperty("os.name").contains("Mac")) {
			// Mac OS X
			System.setProperty("org.lwjgl.librarypath", new File("lwjgl/native/macosx/" + architecture).getAbsolutePath());
		} else if (System.getProperty("os.name").contains("Linux")) {
			// Linux
			System.setProperty("org.lwjgl.librarypath", new File("lwjgl/native/linux/" + architecture).getAbsolutePath());
		} else {
			throw new RuntimeException("Your OS is not supported");
		}
	}

	public GraphicMagic(String title, int width, int height) {
		this.title = title;
		this.width = width;
		this.height = height;
	}

	public void addPanel(Panel p){
		synchronized (panels) {
			panels.add(p);
		}
	}
	
	@Override
	public void run() {
		System.out.println("Hello LWJGL " + Sys.getVersion() + "!");

		try {
			init(width, height, title);
			loop();

			// Release window and window callbacks
			GLFW.glfwDestroyWindow(window);
			keyCallback.release();
		} finally {
			// Terminate GLFW and release the GLFWerrorfun
			GLFW.glfwTerminate();
			errorCallback.release();
		}
	}

	private void init(int width, int height, String title) {
		// Setup an error callback. The default implementation
		// will print the error message in System.err.
		GLFW.glfwSetErrorCallback(errorCallback = Callbacks.errorCallbackPrint(System.err));

		// Initialize GLFW. Most GLFW functions will not work before doing this.
		if (GLFW.glfwInit() != GL11.GL_TRUE)
			throw new IllegalStateException("Unable to initialize GLFW");

		// Configure our window
		GLFW.glfwDefaultWindowHints(); // optional, the current window hints are already the default
		GLFW.glfwWindowHint(GLFW.GLFW_VISIBLE, GL11.GL_FALSE); // the window will stay hidden after creation
		GLFW.glfwWindowHint(GLFW.GLFW_RESIZABLE, GL11.GL_TRUE); // the window will be resizable

		// Create the window
		window = GLFW.glfwCreateWindow(width, height, title, MemoryUtil.NULL, MemoryUtil.NULL);
		if (window == MemoryUtil.NULL)
			throw new RuntimeException("Failed to create the GLFW window");

		// Setup a key callback. It will be called every time a key is pressed, repeated or released.
		GLFW.glfwSetKeyCallback(window, keyCallback = new GLFWKeyCallback() {
			@Override
			public void invoke(long window, int key, int scancode, int action, int mods) {
				if (key == GLFW.GLFW_KEY_ESCAPE && action == GLFW.GLFW_RELEASE)
					GLFW.glfwSetWindowShouldClose(window, GL11.GL_TRUE); // We will detect this in our rendering loop
			}
		});

		// Get the resolution of the primary monitor
		ByteBuffer vidmode = GLFW.glfwGetVideoMode(GLFW.glfwGetPrimaryMonitor());
		// Center our window
		GLFW.glfwSetWindowPos(window, (GLFWvidmode.width(vidmode) - width) / 2, (GLFWvidmode.height(vidmode) - height) / 2);

		// Make the OpenGL context current
		GLFW.glfwMakeContextCurrent(window);
		// Enable v-sync
		GLFW.glfwSwapInterval(1);

		// Make the window visible
		GLFW.glfwShowWindow(window);
	}

	private void loop() {
		// This line is critical for LWJGL's interoperation with GLFW's
		// OpenGL context, or any context that is managed externally.
		// LWJGL detects the context that is current in the current thread,
		// creates the ContextCapabilities instance and makes the OpenGL
		// bindings available for use.
		GLContext.createFromCurrent();

		// Set the clear color
		GL11.glClearColor(0.0f, 0.0f, 0.0f, 0.0f);

		long t = System.currentTimeMillis();

		// double x=0, y=0;

		// Run the rendering loop until the user has attempted to close
		// the window or has pressed the ESCAPE key.
		double halfSpace = 1;

		int fps = 0;

		int oldWidth = 0;
		int oldHeight = 0;
		// double aspect = 1;
		double left = -halfSpace;
		double right = halfSpace;
		double bottom = -halfSpace;
		double top = halfSpace;

		while (GLFW.glfwWindowShouldClose(window) == GL11.GL_FALSE) {

			IntBuffer w = BufferUtils.createIntBuffer(1);
			IntBuffer h = BufferUtils.createIntBuffer(1);
			GLFW.glfwGetWindowSize(window, w, h);
			int width = w.get(0);
			int height = h.get(0);

			if (oldWidth != width || oldHeight != height) {
				oldWidth = width;
				oldHeight = height;

				GL11.glMatrixMode(GL11.GL_PROJECTION); // Select The Projection Matrix
				GL11.glLoadIdentity(); // Reset The Projection Matrix

				GL11.glViewport(0, 0, width, height); // Sets the viewport to get proper ortho display

				GL11.glMatrixMode(GL11.GL_MODELVIEW); // Select The Modelview Matrix

				System.out.println("Intercepted windows resize!");
				System.out.println("New window size " + width + " " + height);
				System.out.println();
			}

			GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT); // clear the framebuffer

			int s = panels.size();
			double panelHeight = (top-bottom)/s;
			double actualBottom = bottom;
			double actualTop = bottom+panelHeight;
			synchronized (panels) {
				for (Panel p: panels){
					p.draw(left, right, actualBottom, actualTop);
					actualBottom = actualTop;
					actualTop+=panelHeight;
				}
			}

			fps++;
			if (System.currentTimeMillis() - t > 1000) {
				System.out.println("fps:"+fps);
				fps=0;
				t = System.currentTimeMillis();
			}

			GLFW.glfwSwapBuffers(window); // swap the color buffers

			// Poll for window events. The key callback above will only be
			// invoked during this call.
			GLFW.glfwPollEvents();
		}
	}

}
