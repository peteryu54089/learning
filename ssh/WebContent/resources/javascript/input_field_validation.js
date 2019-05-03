function get_utf8(field, option) {
    if (option == 'length')
        return unescape(encodeURIComponent(field.value)).length;
    else if (option == 'string')
        return unescape(encodeURIComponent(field.value));
}
function get_label_name(field) {
	var labels = document.getElementsByTagName('label');
	for (var i = 0; i < labels.length; i++){
		if (labels[i].htmlFor !== '' && labels[i].htmlFor == field.name) {
			return labels[i].innerText;
		}
	}
    return field.previousSibling.previousSibling.innerText;
}

function if_length_too_long(fields, length_array) {
    var flag = true;
	for (var i = 0; i < fields.length; i += 1) {
		if (get_utf8(fields[i], 'length') > length_array[i]) {
			var label_name = get_label_name(fields[i]);
			alert(label_name + '過長');
			flag = false;
		}
	}
    return flag;
}

function if_start_end_time(startTime, endTime) {
    startTimeString = get_utf8(startTime, 'string');
    endTimeString = get_utf8(endTime, 'string');
    if (startTimeString && endTimeString && endTimeString < startTimeString) {
        alert('結束日期不可小於開始日期');
        return false;
    }
    return true;
}

function if_semester_valid(semester_field) {
	var semester_string = get_utf8(semester_field, 'string');
	var flag = true;
	if (!/^\d+$/.test(semester_string)){
		flag = false;
	}
	var semester = semester_string.slice(3);
	if (semester != '1' && semester != '2'){
		flag = false;
	}

	if (!flag) alert(get_label_name(semester_field) + '格式錯誤');
	return flag;
}

function if_job_level_valid(job_level) {
	var flag = true;
	var selected_job_level_value = job_level.options[job_level.selectedIndex].value;
	var job_level_array = job_levels.options;

	if (!job_level_array.includes(selected_job_level_value)) {
		flag = false;
		if (!flag) alert(get_label_name(job_level) + '格式錯誤');
	}

	return flag;
}

function if_date_valid(date_field_array) {
	var FLAG = true;
	for (var i = 0; i < date_field_array.length; i++){
        var date_string = get_utf8(date_field_array[i], 'string');
        var flag = true;
		if (!/^\d+$/.test(date_string)){
			flag = false;
		}
        var year = parseInt(date_string.slice(0, 3));
        var month = parseInt(date_string.slice(3, 5));
        var day = parseInt(date_string.slice(5));
        // Check the ranges of month and year
		if (year < 0 || month === 0 || month > 12){
			flag = false;
		}

        var monthLength = [31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31];

        // Adjust for leap years
        if (year % 400 === 0 || (year % 100 !== 0 && year % 4 === 0))
            monthLength[1] = 29;

        // Check the range of the day
        if (!(day > 0 && day <= monthLength[month - 1])) {
            flag = false;
        }
        if (!flag)
			alert(get_label_name(date_field_array[i]) + '格式錯誤');
		FLAG &= flag;
    }
	if (FLAG === 0)
		return false;
	else
		return true;
}

 function validate() {
	 var validation = true;
	 var title = document.forms["myForm"]["title"];
	 var content = document.forms["myForm"]["content"];

	 var test_length_array = [title, content];
	 validation &= if_length_too_long(test_length_array, document_field_length_array);
	 if (validation === 0)
		return false;
	 else
		return true;
}

var nvarchar_to_varchar = 3;

// performance page
// var test_length_array = [unit, startTime, endTime, term, jobLevel, job, content, documentation, video, external_link];
var cadre_field_length_array = [20*nvarchar_to_varchar, 7, 7, 4, 1, 20*nvarchar_to_varchar, 50*nvarchar_to_varchar, 500*nvarchar_to_varchar, 500*nvarchar_to_varchar, 500*nvarchar_to_varchar];
// var test_length_array = [name, item, field, level, award, time, content, type , documentation, video, external_link];
var competition_field_length_array = [40*nvarchar_to_varchar, 20*nvarchar_to_varchar, 2, 1, 20*nvarchar_to_varchar, 7, 50*nvarchar_to_varchar, 1, 500*nvarchar_to_varchar, 500*nvarchar_to_varchar, 500*nvarchar_to_varchar];
// var test_length_array = [code, note, point, result, time, licensenumber, group, content, documentation, video, external_link];
var license_field_length_array = [4, 1, 5, 50*nvarchar_to_varchar, 7, 20*nvarchar_to_varchar, 20*nvarchar_to_varchar, 100*nvarchar_to_varchar, 500*nvarchar_to_varchar, 500*nvarchar_to_varchar, 500*nvarchar_to_varchar];
// var test_length_array = [name, place, startTime, endTime, count, content, documentation, video, external_link];
var volunteer_field_length_array = [20*nvarchar_to_varchar, 20*nvarchar_to_varchar, 7, 7, 5, 100*nvarchar_to_varchar, 500*nvarchar_to_varchar, 500*nvarchar_to_varchar, 500*nvarchar_to_varchar];
// var test_length_array = [name, unit, startTime, endTime, count, content, documentation, video, external_link];
var other_field_length_array = [20*nvarchar_to_varchar, 20*nvarchar_to_varchar, 7, 7, 5, 100*nvarchar_to_varchar, 500*nvarchar_to_varchar, 500*nvarchar_to_varchar, 500*nvarchar_to_varchar];

// student page
var autobiography_field_length_array = [20, 50];
var studyplan_field_length_array = [20, 50];
var document_field_length_array = [20, 50];
