/* http://github.com/mindmup/bootstrap-wysiwyg */
/*global jQuery, $, FileReader*/
/*jslint browser:true*/
(function ($) {
	'use strict';
	var readFileIntoDataUrl = function (fileInfo) {
		var loader = $.Deferred(),
			fReader = new FileReader();
		fReader.onload = function (e) {
			loader.resolve(e.target.result);
		};
		fReader.onerror = loader.reject;
		fReader.onprogress = loader.notify;
		fReader.readAsDataURL(fileInfo);
		return loader.promise();
	};
	$.fn.cleanHtml = function () {
		var html = $(this).html();
		return html && html.replace(/(<br>|\s|<div><br><\/div>|&nbsp;)*$/, '');
	};
	$.fn.wysiwyg_destroy = function(userOptions) {
	    var editor, options, toolbar, toolbarBtnSelector;
	    editor = this;
	    options = $.extend({}, $.fn.wysiwyg.defaults, userOptions);
	    toolbarBtnSelector = "a[data-" + options.commandRole + "],button[data-" + options.commandRole + "],input[type=button][data-" + options.commandRole + "]";
	    editor.off("." + options.eventNamespace);
	    editor.off("paste");
	    $(window).off("." + options.eventNamespace);
	    toolbar = $(options.toolbarSelector);
	    toolbar.find(toolbarBtnSelector).off("." + options.eventNamespace);
	    toolbar.find("[data-toggle=dropdown]").off("." + options.eventNamespace);
	    toolbar.find("input[type=text][data-" + options.commandRole + "]").off("." + options.eventNamespace);
	    toolbar.find("input[type=file][data-" + options.commandRole + "]").off("." + options.eventNamespace);
	    return this;
	};
	$.fn.wysiwyg = function (userOptions) {		
		var editor = this,
			selectedRange,
			options,
			toolbarBtnSelector,
			startContainer,
			startOffset,
			endContainer,
			endOffset,
			updateToolbar = function () {
				if (options.activeToolbarClass) {
					$(options.toolbarSelector).find(toolbarBtnSelector).each(function () {
						var command = $(this).data(options.commandRole);
						if (document.queryCommandState(command)) {
							$(this).addClass(options.activeToolbarClass);
						} else {
							$(this).removeClass(options.activeToolbarClass);
						}
					});
				}
			},
			execCommand = function (commandWithArgs, valueArg) {
				var commandArr = commandWithArgs.split(' '),
					command = commandArr.shift(),
					args = commandArr.join(' ') + (valueArg || '');
				/*if(command == 'unlink') {
					$('#btn-toolbar input[type=text][data-edit]').val('');
					$('#btn-toolbar select[data-edit]').val(0);
				}*/
				if(command == 'createLink') {
					//EQ-179????????????
					var selection = window.getSelection();
					// ???????????? Range
					var newRange = document.createRange(); // ???????????????????????????????????????????????????????????? range ???????????????
					newRange.setStart(startContainer, startOffset);
					newRange.setEnd(endContainer, endOffset);

					// ????????????
					selection.removeAllRanges();
					selection.addRange(newRange);
					
					//document.execCommand('insertHTML', false, '<a href="' + args + '" target="_blank">' + window.getSelection() + '</a>');
					//document.execCommand(command, true, args);
					//????????????
					if(window.getSelection().isCollapsed ) {
						updateToolbar();
						return;
					}
					if(valueArg[0] == 'external') {
						var newLink = document.execCommand(command, 0	, PREFIX_S1_URL + '?c=scene&a=link&id' + valueArg[2] + '&url=' + encodeURIComponent(valueArg[1]));
						var el = window.getSelection().focusNode;
						while (el && el.parentNode) {
							el = el.parentNode;
							if (el.tagName && el.tagName.toLowerCase() == 'a') {
								el.target = '_blank';
								$(el).removeAttr('data');
								break;
							}
						}
						var el2 = window.getSelection().anchorNode;
						while (el2 && el2.parentNode) {
							el2 = el2.parentNode;
							if (el2.tagName && el2.tagName.toLowerCase() == 'a') {
								el2.target = '_blank';
								$(el2).removeAttr('data');
								break;
							}
						}
						/*window.getSelection().focusNode.parentElement.target = '_blank';
						$(window.getSelection().focusNode.parentElement).removeAttr('data');
						window.getSelection().anchorNode.parentElement.target = '_blank';
						$(window.getSelection().anchorNode.parentElement).removeAttr('data');*/
					} else if(valueArg[0] == 'internal') {
						var newLink = document.execCommand(command, 0 , args.split(',')[1]);
						var el = window.getSelection().focusNode;
						while (el && el.parentNode) {
							el = el.parentNode;
							if (el.tagName && el.tagName.toLowerCase() == 'a') {
								$(el).attr('data', args.split(',')[1]);
								break;
							}
						}
						var el2 = window.getSelection().anchorNode;
						while (el2 && el2.parentNode) {
							el2 = el2.parentNode;
							if (el2.tagName && el2.tagName.toLowerCase() == 'a') {
								$(el2).attr('data', args.split(',')[1]);
								break;
							}
						}
						/*var newLink = document.execCommand(command, 0 , args.split(',')[1]);
						$(window.getSelection().focusNode).parent('a').removeAttr('href');
						$(window.getSelection().focusNode).parent('a').attr('data', args.split(',')[1]);
						$(window.getSelection().anchorNode).parent('a').removeAttr('href');
						$(window.getSelection().anchorNode).parent('a').attr('data', args.split(',')[1]);*/
					}
     				//newLink.target = "_blank";
				} else {
					document.execCommand(command, 0, args);
				}				
				updateToolbar();
			},
			bindHotkeys = function (hotKeys) {
				$.each(hotKeys, function (hotkey, command) {
					editor.keydown(hotkey, function (e) {
						if (editor.attr('contenteditable') && editor.is(':visible')) {
							e.preventDefault();
							e.stopPropagation();
							execCommand(command);
						}
					}).keyup(hotkey, function (e) {
						if (editor.attr('contenteditable') && editor.is(':visible')) {
							e.preventDefault();
							e.stopPropagation();
						}
					});
				});
			},
			getCurrentRange = function () {
				var sel = window.getSelection();
				if (sel.getRangeAt && sel.rangeCount) {
					return sel.getRangeAt(0);
				}
			},
			saveSelection = function () {
				selectedRange = getCurrentRange();
			},
			restoreSelection = function () {
				var selection = window.getSelection();
				if (selectedRange) {
					try {
						selection.removeAllRanges();
					} catch (ex) {
						document.body.createTextRange().select();
						document.selection.empty();
					}

					selection.addRange(selectedRange);
				}
			},
			insertFiles = function (files) {
				editor.focus();
				$.each(files, function (idx, fileInfo) {
					if (/^image\//.test(fileInfo.type)) {
						$.when(readFileIntoDataUrl(fileInfo)).done(function (dataUrl) {
							execCommand('insertimage', dataUrl);
						}).fail(function (e) {
							options.fileUploadError("file-reader", e);
						});
					} else {
						options.fileUploadError("unsupported-file-type", fileInfo.type);
					}
				});
			},
			markSelection = function (input, color) {
				restoreSelection();
				if (document.queryCommandSupported('hiliteColor')) {
					document.execCommand('hiliteColor', 0, color || 'transparent');
				}
				saveSelection();
				input.data(options.selectionMarker, color);
			},
			bindToolbar = function (toolbar, options) {
				toolbar.find(toolbarBtnSelector).click(function () {
					restoreSelection();
					editor.focus();
					execCommand($(this).data(options.commandRole));
					saveSelection();
				});
				toolbar.find('[data-toggle=dropdown]').click(restoreSelection);
				var radioValue, elementid;
				toolbar.find('.createLink[data-toggle=dropdown]').click(function() {
					//$('.selected-text').html(getCurrentRange().endContainer.data.substring(getCurrentRange().startOffset, getCurrentRange().endOffset));
					var input;
					var parent = $(getSelection().focusNode).parent();
					//console.log(parent[0])
					/*var el = window.getSelection().focusNode;
					while (el && el.parentNode) {
						el = el.parentNode;
						if (el.tagName && el.tagName.toLowerCase() == 'a') {
							parent = $(el);
							break;
						}
					}*/
					elementid = $(parent).closest('.element').attr('id');
					$('#btn-toolbar input[type=text][data-edit]').attr('id', 'input_' + elementid);
					$('#btn-toolbar select[data-edit]').attr('id', 'select_' + elementid);
					$('#btn-toolbar input[name=external]').attr('id', 'external_' + elementid);
					$('#btn-toolbar input[name=internal]').attr('id', 'internal_' + elementid);
					if(isNaN($('#select_'+ elementid).find('option')[0].value)) {
						$($('#select_'+ elementid).find('option')[0]).remove();
					}
					if(parent.is('a')) {
						if(parent.attr('href') && isNaN(parent.attr('href'))) {
							input = $('#btn-toolbar #input_' + elementid);
							input.val(decodeURIComponent(parent.attr('href').split('url=')[1]));
							$('#btn-toolbar #select_' + elementid).val(0).attr('disabled', true);
							$('#btn-toolbar #external_' + elementid).attr('checked', true);
							$('#btn-toolbar #internal_' + elementid).attr('checked', false);
							radioValue = 'external';
						} else if(parent.attr('data') || !isNaN(parent.attr('href'))) {
							input = $('#btn-toolbar #select_' + elementid);
							var url = PREFIX_URL + '?c=scene&a=pagelist&id=' + input.attr('sceneid') + '?date=' + new Date().getTime();
							$.ajax({
								method: 'GET',
								url: url,
								xhrFields: {
									withCredentials: true
								},
								crossDomain: true
							}).then(function(res) {
								var list = res.list;
								if(res.success) {
									for(var i = 0; i < list.length; i++) {
										if(list[i].id == parent.attr('data')) {
											input.val(list[i].num - 1);
										}
									}
								}
							});
							//input.val(parent.attr('data')-1);
							$('#btn-toolbar #input_' + elementid).val('http://').attr('disabled', true);
							$('#btn-toolbar #internal_' + elementid).attr('checked', true);
							$('#btn-toolbar #external_' + elementid).attr('checked', false);
							radioValue = 'internal';
						}
						/*input.val(decodeURIComponent(parent.attr('href').split('url=')[1]));*/
					} else {
						$('#btn-toolbar #input_' + elementid).val('http://');
						$('#btn-toolbar #select_' + elementid).val(0).attr('disabled', true);
						$('#btn-toolbar #internal_' + elementid).attr('checked', false);
						$('#btn-toolbar #external_' + elementid).attr('checked', true);
						radioValue = 'external';
					}

					//EQ-179??????????????????focus????????????????????????????????????
					var selection = window.getSelection();
					var range = selection.getRangeAt(0);

					// ???????????? Range ?????????
					startContainer = range.startContainer;
					startOffset = range.startOffset;
					endContainer = range.endContainer;
					endOffset = range.endOffset;
				});

				$('#btn-toolbar input[name=external]').change(function() {
					radioValue = this.value;
					$('#btn-toolbar #select_' + elementid).val(0).attr('disabled', true);
					$('#btn-toolbar #input_' + elementid).removeAttr('disabled');
					$('#btn-toolbar #internal_' + elementid).attr('checked', false);
				});
				$('#btn-toolbar input[name=internal]').change(function() {
					radioValue = this.value;
					$('#btn-toolbar #input_' + elementid).val('http://').attr('disabled', true);
					$('#btn-toolbar #select_' + elementid).removeAttr('disabled');
					$('#btn-toolbar #external_' + elementid).attr('checked', false);
				});

				$('a[dropdown-toggle]').click(function() {
					if(radioValue == 'external') {
						var elem = toolbar.find('input[type=text][data-' + options.commandRole + ']');
						var newValue = $(elem).val(); /* ugly but prevents fake double-calls due to selection restoration */
						$(elem).val('');
						var sceneId = $(elem).attr('sceneid');
						restoreSelection();
						var prefix = 'http://';
						if (!/^http:|^https:/.test(newValue))
						{
							newValue = prefix + newValue;
						}
						if (newValue && newValue != prefix) {
							editor.focus();
							execCommand($(elem).data(options.commandRole), [radioValue, newValue, sceneId]);
						}
						saveSelection();
					} else if(radioValue == 'internal') {
						//alert(789);
						var elem = toolbar.find('select[data-' + options.commandRole + ']');
						//var newValue = $(elem).val(); /* ugly but prevents fake double-calls due to selection restoration */
						var newValue = $(elem).attr('pageid');
						$(elem).val('');
						var sceneId = $(elem).attr('sceneid');
						restoreSelection();
						if (newValue) {
							editor.focus();
							execCommand($(elem).data(options.commandRole), [radioValue, parseInt(newValue), sceneId]);
						}
					}
				});
				toolbar.find('input[type=file][data-' + options.commandRole + ']').change(function () {
					restoreSelection();
					if (this.type === 'file' && this.files && this.files.length > 0) {
						insertFiles(this.files);
					}
					saveSelection();
					this.value = '';
				});
			},
			initFileDrops = function () {
				editor.on('dragenter dragover', false)
					.on('drop', function (e) {
						var dataTransfer = e.originalEvent.dataTransfer;
						e.stopPropagation();
						e.preventDefault();
						if (dataTransfer && dataTransfer.files && dataTransfer.files.length > 0) {
							insertFiles(dataTransfer.files);
						}
					});
			};
		options = $.extend({}, $.fn.wysiwyg.defaults, userOptions);
		toolbarBtnSelector = 'a[data-' + options.commandRole + '],button[data-' + options.commandRole + '],input[type=button][data-' + options.commandRole + ']';
		bindHotkeys(options.hotKeys);
		if (options.dragAndDropImages) {
			initFileDrops();
		}
		bindToolbar($(options.toolbarSelector), options);
		editor.attr('contenteditable', true).on('mouseup keyup mouseout', function () {
				saveSelection();
				updateToolbar();
			});
		editor.on('paste',function(e) {
		    e.preventDefault();
		    var text = (e.originalEvent || e).clipboardData.getData('text/plain') || prompt('Paste something..');
		    document.execCommand('insertText', false, text);
		});
		$(window).bind('touchend', function (e) {
			var isInside = (editor.is(e.target) || editor.has(e.target).length > 0),
				currentRange = getCurrentRange(),
				clear = currentRange && (currentRange.startContainer === currentRange.endContainer && currentRange.startOffset === currentRange.endOffset);
			if (!clear || isInside) {
				saveSelection();
				updateToolbar();
			}
		});
		return this;
	};
	$.fn.wysiwyg.defaults = {
		hotKeys: {
			'ctrl+b meta+b': 'bold',
			'ctrl+i meta+i': 'italic',
			'ctrl+u meta+u': 'underline',
			'ctrl+z meta+z': 'undo',
			'ctrl+y meta+y meta+shift+z': 'redo',
			'ctrl+l meta+l': 'justifyleft',
			'ctrl+r meta+r': 'justifyright',
			'ctrl+e meta+e': 'justifycenter',
			'ctrl+j meta+j': 'justifyfull',
			'shift+tab': 'outdent',
			'tab': 'indent'
		},
		toolbarSelector: '[data-role=editor-toolbar]',
		commandRole: 'edit',
		activeToolbarClass: 'btn-info',
		selectionMarker: 'edit-focus-marker',
		selectionColor: 'darkgrey',
		dragAndDropImages: true,
		fileUploadError: function (reason, detail) { console.log("File upload error", reason, detail); }
	};
}(window.jQuery));
