package pers.jay.demo.net

data class Daily(
    var cloud: String? = null,
    var fxDate: String? = null,
    var humidity: String? = null,
    var iconDay: String? = null,
    var iconNight: String? = null,
    var moonPhase: String? = null,
    var moonPhaseIcon: String? = null,
    var moonrise: String? = null,
    var moonset: String? = null,
    var precip: String? = null,
    var pressure: String? = null,
    var sunrise: String? = null,
    var sunset: String? = null,
    var tempMax: String? = null,
    var tempMin: String? = null,
    var textDay: String? = null,
    var textNight: String? = null,
    var uvIndex: String? = null,
    var vis: String? = null,
    var wind360Day: String? = null,
    var wind360Night: String? = null,
    var windDirDay: String? = null,
    var windDirNight: String? = null,
    var windScaleDay: String? = null,
    var windScaleNight: String? = null,
    var windSpeedDay: String? = null,
    var windSpeedNight: String? = null
) {
    override fun toString(): String {
        return "Daily(cloud=$cloud, fxDate=$fxDate, humidity=$humidity, iconDay=$iconDay, iconNight=$iconNight, moonPhase=$moonPhase, moonPhaseIcon=$moonPhaseIcon, moonrise=$moonrise, moonset=$moonset, precip=$precip, pressure=$pressure, sunrise=$sunrise, sunset=$sunset, tempMax=$tempMax, tempMin=$tempMin, textDay=$textDay, textNight=$textNight, uvIndex=$uvIndex, vis=$vis, wind360Day=$wind360Day, wind360Night=$wind360Night, windDirDay=$windDirDay, windDirNight=$windDirNight, windScaleDay=$windScaleDay, windScaleNight=$windScaleNight, windSpeedDay=$windSpeedDay, windSpeedNight=$windSpeedNight)"
    }
}

