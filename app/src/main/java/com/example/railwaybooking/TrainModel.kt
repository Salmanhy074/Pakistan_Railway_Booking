package com.example.railwaybooking

class TrainModel {
    var id: String? = null
    var trainName: String? = null
    var coaches: String? = null
    var engineNumber: String? = null
    var date: String? = null
    var fromStations: String? = null
    var toStations: String? = null
    var intermediateStations: String? = null
    var adminId: String? = null


    constructor()


    constructor(id: String, trainName: String, coaches: String, engineNumber: String, date: String, fromStations: String, toStations: String, intermediateStations: String, adminId: String) {
        this.id = id
        this.trainName = trainName
        this.coaches = coaches
        this.engineNumber = engineNumber
        this.date = date
        this.fromStations = fromStations
        this.toStations = toStations
        this.intermediateStations = intermediateStations
        this.adminId = adminId





    }



}