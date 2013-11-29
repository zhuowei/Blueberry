if (!window.Blueberry) {
	window.Blueberry = {}
}

Blueberry.generateCallbackBlock = function(block) {
  // Define a procedure with a return value.
  var funcName = block.functionName_
  var branch = Blockly.JavaScript.statementToCode(block, 'STACK');
  if (Blockly.JavaScript.INFINITE_LOOP_TRAP) {
    branch = Blockly.JavaScript.INFINITE_LOOP_TRAP.replace(/%1/g,
        '\'' + block.id + '\'') + branch;
  }
  var returnValue = Blockly.JavaScript.valueToCode(block, 'RETURN',
      Blockly.JavaScript.ORDER_NONE) || '';
  if (returnValue) {
    returnValue = '  return ' + returnValue + ';\n';
  }
  var args = [];
  for (var x = 0; x < block.arguments_.length; x++) {
    args[x] = Blockly.JavaScript.variableDB_.getName(block.getTitleValue(block.arguments_[x]),
        Blockly.Variables.NAME_TYPE);
  }
  var code = 'function ' + funcName + '(' + args.join(', ') + ') {\n' +
      branch + returnValue + '}';
  code = Blockly.JavaScript.scrub_(block, code);
  Blockly.JavaScript.definitions_[funcName] = code;
  return null;
};

Blueberry.CallbackBlock = {}
Blueberry.CallbackBlock.getVars = function() {
  console.log(this);
  if (!this.arguments_) return [];
  var retval = new Array(this.arguments_.length);
  for (var i = 0; i < this.arguments_.length; i++) {
    retval[i] = this.getTitleValue(this.arguments_[i])
  }
  return retval;
}
Blueberry.CallbackBlock.renameVar = function(oldName, newName) {
  for (var i = 0; i < this.arguments_.length; i++) {
    if (Blockly.Names.equals(oldName, this.getTitleValue(this.arguments_[i]))) {
      this.setTitleValue(newName, this.arguments_[i]);
    }
  }
}

Blockly.JavaScript['text_print'] = function(block) {
  // Print statement.
  var argument0 = Blockly.JavaScript.valueToCode(block, 'TEXT',
      Blockly.JavaScript.ORDER_NONE) || '\'\'';
  return 'clientMessage(' + argument0 + ');\n';
};
