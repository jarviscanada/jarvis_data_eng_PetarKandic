var whom = analyzeContent("body{blabla} a{color:#fff} a{ padding:0}");
console.log(analyzeContent("body{blabla} a{color:#fff} a{ padding:0}"));
function analyzeContent(content) {
    var result = {
        contentType: "UNKNOWN"
    };
    // Check for HTML content
    if (/<[a-z][\s\S]*>/i.test(content)) {
        result.contentType = "HTML";
        result.tags = {};
        // Match all HTML tags
        var tags = content.match(/<\/?([a-z]+)[\s>]/gi);
        if (tags) {
            tags.forEach(function (tag) {
                // Remove </> characters and convert to lowercase
                var tagName = tag.replace(/[<\/>]/g, '').toLowerCase();
                result.tags[tagName] = (result.tags[tagName] || 0) + 1;
            });
        }
    }
    // Check for CSS content
    else if (/\{[\s\S]*?\}/.test(content)) {
        result.contentType = "CSS";
        result.cssTargets = {};
        // Match all CSS targets
        var targets = content.match(/[a-z0-9.#:-]+(?=\s*\{)/gi);
        if (targets) {
            targets.forEach(function (target) {
                result.cssTargets[target] = (result.cssTargets[target] || 0) + 1;
            });
        }
    }
    // Text content
    else {
        result.contentType = "TEXT";
        result.lineNumber = (content.match(/\n/g) || []).length + 1;
    }
    return result;
}
