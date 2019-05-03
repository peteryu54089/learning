window.DateUtils = {
    toMingoDateTime: function (date) {
        date = DateUtils._processDate(date);

        return `${DateUtils.toMingoDate(date)} ${DateUtils.formatTime(date)}`;
    },

    toMingoDate: function (date) {
        date = DateUtils._processDate(date);

        let dateArr = [
            date.getFullYear() - 1911,
            date.getMonth() + 1,
            date.getDate()
        ].map(x => x.toString().padStart(2, '0'));

        return `${dateArr.join('-')}`;
    },

    formatDate: function (date) {
        date = DateUtils._processDate(date);

        let timeArr = [
            date.getFullYear(),
            date.getMonth() + 1,
            date.getDate()
        ].map(x => x.toString().padStart(2, '0'));

        return `${timeArr.join('-')}`
    },

    formatTime: function (date) {
        date = DateUtils._processDate(date);

        let timeArr = [
            date.getHours(),
            date.getMinutes(),
            // date.getSeconds(),
        ].map(x => x.toString().padStart(2, '0'));

        return `${timeArr.join(':')}`
    },

    toInputValue: function (date) {
        date = DateUtils._processDate(date);
        return DateUtils.formatDate(date) + 'T' + DateUtils.formatTime(date);
    },

    _processDate: function (date) {
        if (typeof date === 'string')
            date = Date.parse(date);
        if (typeof date === 'number')
            date = new Date(date);

        return date;
    }
};

/**
 * @param {FormData} formData
 * @param {string} key
 */
function handleSizeInput(formData, key) {
    let val = formData.get(key);
    formData.delete(key);
    formData.append(key, val << 20);
}

$(function () {
    $('input.size-input[type="number"]').each(function () {
        this.value >>= 20;
    });
});