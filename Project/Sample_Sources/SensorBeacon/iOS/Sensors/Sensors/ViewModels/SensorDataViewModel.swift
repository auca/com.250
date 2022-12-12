//
//  SensorDataViewModel.swift
//  Sensors
//
//  Created by Dmitrii Toksaitov on 12/12/22.
//

import Foundation

@MainActor
class SensorDataViewModel: ObservableObject {
    @Published var data: ReplyModel? = nil
    @Published var requestError: String? = nil

    func loadData() async {
        requestError = nil

        data = await performPostRequest(
            endpoint: "\(serviceBaseURL)/api/ds/query",
            body: SensorDataRequestPostBody(
                queries: [QueriesPostBody(
                    datasource: [
                        "type": "influxdb",
                        "uid": "ihgUkG54k"
                    ],
                    query: "from(bucket:\"SensorData\")\n    |> range(start: -15m)\n    |> filter(fn: (r) => r[\"_measurement\"] == \"temperature\")"
                )],
                from: "now-15m"
            ),
            onServiceError: { self.requestError = $0 }
        )
    }
}
