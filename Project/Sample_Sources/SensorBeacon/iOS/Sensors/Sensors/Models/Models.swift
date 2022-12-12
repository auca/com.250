//
//  Models.swift
//  Sensors
//
//  Created by Dmitrii Toksaitov on 12/12/22.
//

import Foundation

// Request Models

struct QueriesPostBody: Encodable {
    let datasource: [String: String]
    let query: String
}

struct SensorDataRequestPostBody: Encodable {
    let queries: [QueriesPostBody]
    let from: String
}

// Reply Models

struct SensorDataModel: Codable {
    var values: [[Double]]
}

struct FrameModel: Codable {
    var data: SensorDataModel
}

struct AModel: Codable {
    var frames: [FrameModel]
}

struct ResultsModel: Codable {
    var A: AModel
}

struct ReplyModel: Codable {
    var results: ResultsModel
}
