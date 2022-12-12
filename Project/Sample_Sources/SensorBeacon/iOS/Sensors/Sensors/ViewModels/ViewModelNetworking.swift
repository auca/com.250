//
//  ViewModelNetworking.swift
//  Sensors
//
//  Created by Dmitrii Toksaitov on 12/12/22.
//

import Foundation

func performPostRequest<Model>(
         endpoint: String,
         onServiceError: ((String) -> Void)? = nil
     ) async -> Model? where Model : Decodable {
         await performPostRequest(endpoint: endpoint, body: nil as ServiceEmptyBody?, onServiceError: onServiceError)
}

func performPostRequest<BodyType, Model>(
         endpoint: String,
         body: BodyType? = nil,
         onServiceError: ((String) -> Void)? = nil
     ) async -> Model? where BodyType: Encodable, Model: Decodable {
    do {
        guard let url = URL(string: endpoint) else {
            throw ServiceError.error("Bad URL \(endpoint)")
        }

        var request = URLRequest(url: url, cachePolicy: .reloadIgnoringLocalCacheData)
        request.httpMethod = "POST"
        request.setValue("application/json", forHTTPHeaderField: "Accept")
        request.setValue("Bearer \(token)", forHTTPHeaderField: "Authorization")
        if let body = body {
            request.httpBody = try JSONEncoder().encode(body)
            request.setValue("application/json", forHTTPHeaderField: "Content-Type")
        }

        let (data, response) = try await URLSession.shared.data(for: request)
        let httpResponse = response as! HTTPURLResponse
        if httpResponse.statusCode == 400 {
            let serviceError = try JSONDecoder().decode(ServiceErrorReplyBody.self, from: data)
            DispatchQueue.main.async {
                onServiceError?(serviceError.message)
            }

            throw ServiceError.error("Server error: '\(serviceError.message)'")
        } else if httpResponse.statusCode != 200 {
            throw ServiceError.error("Failed to make a request to \(endpoint)")
        }

        return try JSONDecoder().decode(Model.self, from: data)
    } catch {
        print(error)

        return nil
    }
}

func performGetRequest<Model>(
         endpoint: String,
         onServiceError: ((String) -> Void)? = nil
     ) async -> Model? where Model : Decodable {
    do {
        guard let url = URL(string: endpoint) else {
            throw ServiceError.error("Bad URL \(endpoint)")
        }

        var request = URLRequest(url: url)
        request.setValue("application/json", forHTTPHeaderField: "Accept")
        request.setValue("Bearer \(token)", forHTTPHeaderField: "Authorization")
        let (data, response) = try await URLSession.shared.data(for: request)

        let httpResponse = response as! HTTPURLResponse
        if httpResponse.statusCode == 400 {
            let serviceError = try JSONDecoder().decode(ServiceErrorReplyBody.self, from: data)
            DispatchQueue.main.async {
                onServiceError?(serviceError.message)
            }

            throw ServiceError.error("Server error: '\(serviceError.message)'")
        } else if httpResponse.statusCode != 200 {
            throw ServiceError.error("Failed to make a request to \(endpoint)")
        }

        return try JSONDecoder().decode(Model.self, from: data)
    } catch {
        print(error)

        return nil
    }
}
