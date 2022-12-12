//
//  ContentView.swift
//  Sensors
//
//  Created by Dmitrii Toksaitov on 11/12/22.
//

import SwiftUI
import Charts

public class DateValueFormatter: NSObject, AxisValueFormatter {
    private let dateFormatter = DateFormatter()

    override init() {
        super.init()
        dateFormatter.dateFormat = "dd MMM HH:mm"
    }

    public func stringForValue(_ value: Double, axis: AxisBase?) -> String {
        return dateFormatter.string(from: Date(timeIntervalSince1970: value))
    }
}

struct SensorDataChartView: UIViewRepresentable {
    // The data to be displayed on the chart
    let data: SensorDataModel

    func makeUIView(context: Context) -> LineChartView {
        let chartView = LineChartView()
        chartView.gridBackgroundColor = .white
        chartView.noDataText = "No data available"
        chartView.drawGridBackgroundEnabled = true
        chartView.dragEnabled = true
        chartView.setScaleEnabled(true)
        chartView.pinchZoomEnabled = true
        chartView.rightAxis.enabled = false
        chartView.xAxis.valueFormatter = DateValueFormatter()

        let dataSet = LineChartDataSet(entries: data.values[1].enumerated().map { (arg) -> ChartDataEntry in
            let (i, value) = arg
            return ChartDataEntry(x: data.values[0][i], y: value)
        }, label: "Temperature")
        dataSet.drawFilledEnabled = true
        dataSet.fillColor = .blue
        dataSet.fillAlpha = 0.5

        chartView.data = LineChartData(dataSet: dataSet)

        return chartView
    }

    func updateUIView(_ chartView: LineChartView, context: Context) {
        let dataSet = LineChartDataSet(entries: data.values[1].enumerated().map { (arg) -> ChartDataEntry in
            let (i, value) = arg
            return ChartDataEntry(x: data.values[0][i], y: value)
        }, label: "Temperature")
        dataSet.drawFilledEnabled = true
        dataSet.fillColor = .blue
        dataSet.fillAlpha = 0.5
        chartView.data = LineChartData(dataSet: dataSet)
    }
}

struct ContentView: View {
    @StateObject var sensorDataViewModel = SensorDataViewModel()

    var body: some View {
        VStack {
            if let data = sensorDataViewModel.data?.results.A.frames[0].data {
                SensorDataChartView(data: data)
            } else {
                Text("Loading...")
            }
        }.task {
            await sensorDataViewModel.loadData()
        }
    }
}

struct ContentView_Previews: PreviewProvider {
    static var previews: some View {
        ContentView()
    }
}
