package net.zhuoweizhang.mcpelauncher.doclet;

import com.sun.javadoc.*;
import java.io.*;

public class BlueberryDoclet {

	public static PrintWriter blockOut, toolboxOut;

	public static LanguageVersion languageVersion() {
		return LanguageVersion.JAVA_1_5;
	}

	private static void processScriptManager(ClassDoc scriptManagerClass) {
		ClassDoc[] innerClasses = scriptManagerClass.innerClasses(false);
		for (ClassDoc d: innerClasses) {
			if (!d.superclassType().qualifiedTypeName().equals("org.mozilla.javascript.ScriptableObject"))
				continue;
			if (!d.simpleTypeName().contains("Api") && !d.simpleTypeName().contains("BlockHostObject"))
				continue;
			generateBlocks(d);
		}
	}

	private static void generateBlocks(ClassDoc apiClass) {
		String modPEPackageJavaName = apiClass.simpleTypeName();
		String modPEPackageName = modPEPackageJavaName.replace("Native", "").replace("Api", "").replace("BlockHostObject", "");
		String categoryName = modPEPackageName.length() == 0? "Common": modPEPackageName;
		toolboxOut.println("<category name=\"" + categoryName + "\">");
		MethodDoc[] methods = apiClass.methods();
		for (MethodDoc d: methods) {
			if (!isApiMethod(d)) continue;
			generateBlockForMethod(d);
		}
		toolboxOut.println("</category>");
	}

	private static boolean isApiMethod(MethodDoc method) {
		AnnotationDesc[] annotations = method.annotations();
		for (AnnotationDesc desc: annotations) {
			if (desc.annotationType().simpleTypeName().indexOf("JS") == 0) return true;
		}
		return false;
	}

	private static void generateBlockForMethod(MethodDoc method) {
		String modPEPackageJavaName = method.containingClass().simpleTypeName();
		String modPEPackageName = modPEPackageJavaName.replace("Native", "").replace("Api", "").replace("BlockHostObject", "");
		String methodName = method.name();
		String methodBegin = modPEPackageName.length() == 0? methodName: modPEPackageName + "." + methodName;
		String blockName = "modpe_" + (modPEPackageName.length() == 0? methodName: modPEPackageName + "_" + methodName);
		//Let's go!
		blockOut.println(
"Blockly.Blocks[\"" + blockName + "\"] = {");
		blockOut.println(
"  init: function() {");
		blockOut.println(
"    this.setHelpUrl('http://www.example.com/');");
		blockOut.println(
"    this.setColour(20);");
		blockOut.println(
"    this.appendDummyInput()");
		blockOut.println(
"        .appendTitle(\"" + methodBegin + "\");");
/*		System.out.println(
"    this.appendValueInput(\"NAME\")"
"        .setCheck(\"Number\")"
"        .appendTitle(\"x\");"
"    this.setOutput(true, \"Number\");"
"    this.setTooltip('');"*/
		blockOut.println(
"  }");
		blockOut.println(
"};");
		//now the XML
		toolboxOut.println("<block type=\"" + blockName + "\"></block>");
	}

	public static boolean start(RootDoc root) {
		try {
			blockOut = new PrintWriter(new File("blueberry_api_call_blocks.js"));
			toolboxOut = new PrintWriter(new File("toolbox.xml"));
			ClassDoc scriptManagerClass = root.classNamed("net.zhuoweizhang.mcpelauncher.ScriptManager");
			processScriptManager(scriptManagerClass);
			blockOut.close();
			toolboxOut.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return true;
	}
}
