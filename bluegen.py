script_callback_block_template = """
Blockly.Blocks["BLOCK_NAME"] = {
  init: function() {
    this.setHelpUrl('http://www.example.com/');
    this.setColour(120);
    this.appendDummyInput()
        .appendTitle("BLOCK_DISPLAY_NAME");
BLOCK_PARAMS
    this.appendStatementInput("STACK");
    this.setTooltip('');
    BLOCK_SET_PARAMS_ARRAY
    this.functionName_ = "BLOCK_OUTPUT_FUNCTION_NAME"
  },
  getVars: Blueberry.CallbackBlock.getVars,
  renameVar: Blueberry.CallbackBlock.renameVar,
  getProcedureDef: function() {
    return [this.functionName_, this.getVars(), false];
  }
};
Blockly.JavaScript["BLOCK_NAME"] = Blueberry.generateCallbackBlock;
"""


def genCallbacks():
	callbacks = []
	with open("callbacks.txt") as callback_file:

		for l in callback_file:
			parts = l.strip().split("|")
			methodParams = parts[1].split(",")
			while '' in methodParams:
				methodParams.remove('')
			methodDesc = {
				"name": parts[0],
				"params": methodParams,
				"display_name": parts[2]
			}
			callbacks.append(methodDesc)
	print(callbacks)
	blockdefs = []
	block_names_for_toolbox_callback = []
	for c in callbacks:
		params = []
		paramArrayStr = []
		for p in c["params"]:
			params.append("        .appendTitle(new Blockly.FieldVariable(\"" + p + "\"), \"arg_" + p + "\")")
			paramArrayStr.append("arg_" + p)
		print(params)
		param_array_str = "this.arguments_ = " + str(paramArrayStr)
		param_str = "    this.appendDummyInput()\n" + "\n".join(params) if len(params) > 0 else ""
		outstr = script_callback_block_template.replace("BLOCK_NAME", "modpe_callback_" + c["name"]). \
			replace("BLOCK_DISPLAY_NAME", c["display_name"]). \
			replace("BLOCK_PARAMS", param_str). \
			replace("BLOCK_SET_PARAMS_ARRAY", param_array_str). \
			replace("BLOCK_OUTPUT_FUNCTION_NAME", c["name"])
		blockdefs.append(outstr)
		genstr = genCallbackGenerator(c)
		block_names_for_toolbox_callback.append("modpe_callback_" + c["name"])

		print(genstr)
	with open("blueberry_callbacks.js", "w") as outfile:
		print("\n".join(blockdefs), file=outfile)

	genSoyTemplates([{"name": "Callbacks", "blocks": block_names_for_toolbox_callback}])

def genCallbackGenerator(c):
	return ""

def genSoyTemplates(inparams):
	print("{template .blueberry_toolbox}")
	for cat in inparams:
		print("<category name=\"" + cat["name"] + "\">")
		for b in cat["blocks"]:
			print("<block type=\"" + b + "\"></block>")
		print("</category>")
	print("{/template}")

genCallbacks()
