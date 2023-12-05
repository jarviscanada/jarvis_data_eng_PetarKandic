let whom = analyzeContent("body{blabla} a{color:#fff} a{ padding:0}")
console.log(analyzeContent("body{blabla} a{color:#fff} a{ padding:0}"))
//aHeader.textContent = whom.contentType;

type AnalyzeContentResult = {
    contentType: string;
    tags?: { [key: string]: number };
    cssTargets?: { [key: string]: number };
    lineNumber?: number;
};
function analyzeContent(content): AnalyzeContentResult {

    const result: AnalyzeContentResult = {
        contentType: "UNKNOWN"
    };

    // Check for HTML content
    if (/<[a-z][\s\S]*>/i.test(content)) {
        result.contentType = "HTML";
        result.tags = {};

        // Match all HTML tags
        const tags = content.match(/<\/?([a-z]+)[\s>]/gi);
        if (tags) {
            tags.forEach(tag => {
                // Remove </> characters and convert to lowercase
                const tagName = tag.replace(/[<\/>]/g, '').toLowerCase();
                result.tags[tagName] = (result.tags[tagName] || 0) + 1;
            });
        }
    }
    // Check for CSS content
    else if (/\{[\s\S]*?\}/.test(content)) {
        result.contentType = "CSS";
        result.cssTargets = {};

        // Match all CSS targets
        const targets = content.match(/[a-z0-9.#:-]+(?=\s*\{)/gi);
        if (targets) {
            targets.forEach(target => {
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